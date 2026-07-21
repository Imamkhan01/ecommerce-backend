package ecommerce_baceknd.category.service;

import ecommerce_baceknd.category.CategoryResponse;
import ecommerce_baceknd.category.dto.CategoryRequest;
import ecommerce_baceknd.category.entity.Category;
import ecommerce_baceknd.category.repo.CategoryRepository;
import ecommerce_baceknd.common.PageResponse;
import ecommerce_baceknd.exception.exceptionname.DuplicateResourceException;
import ecommerce_baceknd.exception.exceptionname.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
@Service
public class CategoryServiceImpl implements CategoryService{

    private static final Logger log =
            LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    // ==================== CREATE ====================

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {

        String name = request.getName().trim();

        log.info("Creating category. name={}", name);

        if (categoryRepository.existsByNameIgnoreCase(name)) {

            log.warn("Category creation failed. Duplicate name={}", name);

            throw new DuplicateResourceException(
                    "Category already exists with name: " + name
            );
        }

        String slug = generateSlug(name);

        if (categoryRepository.existsBySlug(slug)) {

            log.warn("Category creation failed. Duplicate slug={}", slug);

            throw new DuplicateResourceException(
                    "Category already exists with slug: " + slug
            );
        }

        Category category = new Category();

        category.setName(name);
        category.setSlug(slug);
        category.setDescription(request.getDescription());

        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        Category savedCategory = categoryRepository.save(category);

        log.info(
                "Category created successfully. id={}, slug={}",
                savedCategory.getId(),
                savedCategory.getSlug()
        );

        return mapToResponse(savedCategory);
    }


    // ==================== GET BY ID ====================

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {

        log.debug("Fetching category. id={}", id);

        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {

                    log.warn("Category not found. id={}", id);

                    return new ResourceNotFoundException(
                            "Category not found with id: " + id
                    );
                });

        return mapToResponse(category);
    }


    // ==================== GET BY SLUG ====================

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryBySlug(String slug) {

        log.debug("Fetching category. slug={}", slug);

        Category category = categoryRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> {

                    log.warn("Category not found. slug={}", slug);

                    return new ResourceNotFoundException(
                            "Category not found with slug: " + slug
                    );
                });

        return mapToResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> getAllCategories(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        // Page negative nahi hona chahiye
        if (page < 0) {
            page = 0;
        }

        // Size minimum 1
        if (size < 1) {
            size = 10;
        }

        // Ek request me maximum 100 records
        if (size > 100) {
            size = 100;
        }

        // Sirf in fields par sorting allow hogi
        List<String> allowedSortFields =
                List.of(
                        "id",
                        "name",
                        "createdAt",
                        "updatedAt"
                );

        // Invalid sort field aaye to default name
        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "name";
        }

        // Invalid direction aaye to default asc
        if (!sortDir.equalsIgnoreCase("asc")
                && !sortDir.equalsIgnoreCase("desc")) {

            sortDir = "asc";
        }

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        // Public API me sirf ACTIVE categories
        Page<Category> categoryPage =
                categoryRepository.findByActiveTrue(pageable);

        List<CategoryResponse> content =
                categoryPage
                        .getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return new PageResponse<>(
                content,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isFirst(),
                categoryPage.isLast()
        );
    }

    // ==================== UPDATE ====================

    @Override
    @Transactional
    public CategoryResponse updateCategory(
            Long id,
            CategoryRequest request) {

        log.info("Updating category. id={}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Category update failed. Category not found. id={}", id);

                    return new ResourceNotFoundException(
                            "Category not found with id: " + id
                    );
                });

        String newName = request.getName().trim();

        /*
         * Duplicate name tabhi check karna hai
         * jab category ka name actually change ho.
         */
        if (!category.getName().equalsIgnoreCase(newName)
                && categoryRepository.existsByNameIgnoreCase(newName)) {

            log.warn(
                    "Category update failed. Duplicate name={}, id={}",
                    newName,
                    id
            );

            throw new DuplicateResourceException(
                    "Category already exists with name: " + newName
            );
        }

        String newSlug = generateSlug(newName);

        /*
         * Slug change hua hai to duplicate slug check karo.
         */
        if (!category.getSlug().equals(newSlug)
                && categoryRepository.existsBySlug(newSlug)) {

            log.warn(
                    "Category update failed. Duplicate slug={}, id={}",
                    newSlug,
                    id
            );

            throw new DuplicateResourceException(
                    "Category already exists with slug: " + newSlug
            );
        }

        category.setName(newName);
        category.setSlug(newSlug);
        category.setDescription(request.getDescription());

        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        Category updatedCategory = categoryRepository.save(category);

        log.info(
                "Category updated successfully. id={}, slug={}",
                updatedCategory.getId(),
                updatedCategory.getSlug()
        );

        return mapToResponse(updatedCategory);
    }


    // ==================== DELETE ====================

    @Override
    @Transactional
    public void deleteCategory(Long id) {

        log.info("Deleting category. id={}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Category deletion failed. Category not found. id={}", id);

                    return new ResourceNotFoundException(
                            "Category not found with id: " + id
                    );
                });

        categoryRepository.delete(category);

        log.info("Category deleted successfully. id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> getAllCategoriesForAdmin(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        // Invalid page protection
        if (page < 0) {
            page = 0;
        }

        // Invalid size protection
        if (size < 1) {
            size = 10;
        }

        // Maximum 100 records per page
        if (size > 100) {
            size = 100;
        }

        // Allowed sorting fields
        List<String> allowedSortFields =
                List.of(
                        "id",
                        "name",
                        "createdAt",
                        "updatedAt"
                );

        // Invalid sort field
        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "name";
        }

        // Invalid sort direction
        if (!sortDir.equalsIgnoreCase("asc")
                && !sortDir.equalsIgnoreCase("desc")) {

            sortDir = "asc";
        }

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );

        // ADMIN - active + inactive dono
        Page<Category> categoryPage =
                categoryRepository.findAll(pageable);

        List<CategoryResponse> content =
                categoryPage
                        .getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return new PageResponse<>(
                content,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isFirst(),
                categoryPage.isLast()
        );
    }

    @Override
    @Transactional
    public CategoryResponse updateCategoryStatus(
            Long id,
            Boolean active) {

        log.info(
                "Updating category status. id={}, active={}",
                id,
                active
        );

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Category not found for status update. id={}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Category not found with id: " + id
                    );
                });

        category.setActive(active);

        Category updatedCategory =
                categoryRepository.save(category);

        log.info(
                "Category status updated. id={}, active={}",
                updatedCategory.getId(),
                updatedCategory.getActive()
        );

        return mapToResponse(updatedCategory);
    }


    // ==================== SLUG GENERATION ====================

    private String generateSlug(String name) {

        return name
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }


    // ==================== ENTITY -> RESPONSE DTO ====================

    private CategoryResponse mapToResponse(Category category) {

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());
        response.setSlug(category.getSlug());
        response.setDescription(category.getDescription());
        response.setActive(category.getActive());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());

        return response;
    }
}

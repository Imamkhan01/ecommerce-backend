package ecommerce_baceknd.category.service;


import ecommerce_baceknd.category.CategoryResponse;
import ecommerce_baceknd.category.dto.CategoryRequest;
import ecommerce_baceknd.common.PageResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse getCategoryBySlug(String slug);

    PageResponse<CategoryResponse> getAllCategories(
            int page,
            int size,
            String sortBy,
            String sortDir
    );
    void deleteCategory(Long id);
    PageResponse<CategoryResponse> getAllCategoriesForAdmin(
            int page,
            int size,
            String sortBy,
            String sortDir
    );
    CategoryResponse updateCategoryStatus(
            Long id,
            Boolean active
    );

}
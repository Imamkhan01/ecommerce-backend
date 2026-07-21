package ecommerce_baceknd.category.controller;

import ecommerce_baceknd.category.CategoryResponse;
import ecommerce_baceknd.category.dto.CategoryRequest;
import ecommerce_baceknd.category.dto.CategoryStatusRequest;
import ecommerce_baceknd.category.service.CategoryService;
import ecommerce_baceknd.common.ApiResponse;
import ecommerce_baceknd.common.PageResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    // ================= CREATE - ADMIN =================

    @PostMapping("/admin/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse category =
                categoryService.createCategory(request);

        ApiResponse<CategoryResponse> response =
                new ApiResponse<>(
                        true,
                        "Category created successfully",
                        category
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ================= UPDATE CATEGORY - ADMIN =================

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse category =
                categoryService.updateCategory(id, request);

        ApiResponse<CategoryResponse> response =
                new ApiResponse<>(
                        true,
                        "Category updated successfully",
                        category
                );

        return ResponseEntity.ok(response);
    }


    // ================= DELETE CATEGORY - ADMIN =================

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long id) {

        categoryService.deleteCategory(id);

        ApiResponse<Void> response =
                new ApiResponse<>(
                        true,
                        "Category deleted successfully",
                        null
                );

        return ResponseEntity.ok(response);
    }


    // ================= GET ALL CATEGORIES - PUBLIC =================

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>>
    getAllCategories(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "name") String sortBy,

            @RequestParam(defaultValue = "asc") String sortDir) {

        PageResponse<CategoryResponse> categories =
                categoryService.getAllCategories(
                        page,
                        size,
                        sortBy,
                        sortDir
                );

        ApiResponse<PageResponse<CategoryResponse>> response =
                new ApiResponse<>(
                        true,
                        "Categories fetched successfully",
                        categories
                );

        return ResponseEntity.ok(response);
    }

    // ================= GET CATEGORY BY ID - PUBLIC =================

    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable Long id) {

        CategoryResponse category =
                categoryService.getCategoryById(id);

        ApiResponse<CategoryResponse> response =
                new ApiResponse<>(
                        true,
                        "Category fetched successfully",
                        category
                );

        return ResponseEntity.ok(response);
    }


    // ================= GET CATEGORY BY SLUG - PUBLIC =================

    @GetMapping("/categories/slug/{slug}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlug(
            @PathVariable String slug) {

        CategoryResponse category =
                categoryService.getCategoryBySlug(slug);

        ApiResponse<CategoryResponse> response =
                new ApiResponse<>(
                        true,
                        "Category fetched successfully",
                        category
                );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/admin/categories")
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>>
    getAllCategoriesForAdmin(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "name") String sortBy,

            @RequestParam(defaultValue = "asc") String sortDir) {

        PageResponse<CategoryResponse> categories =
                categoryService.getAllCategoriesForAdmin(
                        page,
                        size,
                        sortBy,
                        sortDir
                );

        ApiResponse<PageResponse<CategoryResponse>> response =
                new ApiResponse<>(
                        true,
                        "Categories fetched successfully",
                        categories
                );

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/admin/categories/{id}/status")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategoryStatus(
            @PathVariable Long id,
            @Valid @RequestBody CategoryStatusRequest request) {

        CategoryResponse category =
                categoryService.updateCategoryStatus(
                        id,
                        request.getActive()
                );

        String message = request.getActive()
                ? "Category activated successfully"
                : "Category deactivated successfully";

        ApiResponse<CategoryResponse> response =
                new ApiResponse<>(
                        true,
                        message,
                        category
                );

        return ResponseEntity.ok(response);
    }
}

package ecommerce_baceknd.category.service;


import ecommerce_baceknd.category.CategoryResponse;
import ecommerce_baceknd.category.dto.CategoryRequest;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse getCategoryBySlug(String slug);

    List<CategoryResponse> getAllCategories();

    void deleteCategory(Long id);
}
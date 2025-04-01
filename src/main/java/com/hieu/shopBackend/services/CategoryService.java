package com.hieu.shopBackend.services;

import com.hieu.shopBackend.dtos.requests.category.CategoryRequest;
import com.hieu.shopBackend.models.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequest category);

    Category getCategoryById(Long categoryId);

    List<Category> getAllCategories();

    Category updateCategory(Long categoryId, CategoryRequest category);

    void deleteCategory(Long categoryId);
}

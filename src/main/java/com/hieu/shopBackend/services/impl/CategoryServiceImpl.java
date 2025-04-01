package com.hieu.shopBackend.services.impl;

import com.hieu.shopBackend.dtos.requests.category.CategoryRequest;
import com.hieu.shopBackend.models.Category;
import com.hieu.shopBackend.repositories.CategoryRepository;
import com.hieu.shopBackend.services.CategoryService;
import com.hieu.shopBackend.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryRequest category) {
        Category newCategory =Category.builder()
                .name(category.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).
                orElseThrow(() -> new RuntimeException(MessageKeys.NOT_FOUND));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(Long categoryId, CategoryRequest category) {
        Category oldCategory = getCategoryById(categoryId);
        oldCategory.setName(category.getName());
        return categoryRepository.save(oldCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}

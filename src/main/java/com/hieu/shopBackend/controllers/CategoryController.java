package com.hieu.shopBackend.controllers;

import com.hieu.shopBackend.dtos.requests.category.CategoryRequest;
import com.hieu.shopBackend.dtos.responses.ApiResponse;
import com.hieu.shopBackend.models.Category;
import com.hieu.shopBackend.services.CategoryService;
import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        try {
            Category newCategory = categoryService.createCategory(categoryRequest);

            return ResponseEntity.ok(ApiResponse.builder().success(true)
                    .message(MessageKeys.CREATE_CATEGORIES_SUCCESS)
                    .result(newCategory)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.builder()
                    .error(e.getMessage())
                    .message(MessageKeys.CREATE_CATEGORIES_FAILED).build());
        }
    }

    // ai cũng có thể lấy ra danh sách các danh mục sản phẩm
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        /*kafka get all category*/
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .result(categories)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategories(@PathVariable("id") Long id,
                                              @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok(ApiResponse.builder().success(true)
                .message(MessageKeys.UPDATE_CATEGORIES_SUCCESS)
                .result(category)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.builder().success(true)
                    .message(MessageKeys.DELETE_CATEGORIES_SUCCESS)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message(MessageKeys.DELETE_CATEGORIES_SUCCESS)
                    .build());
        }
    }
}

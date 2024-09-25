package com.minhbui.ecommerce.controller;

import com.minhbui.ecommerce.dto.request.ApiResponse;
import com.minhbui.ecommerce.dto.request.CategoryCreationRequest;
import com.minhbui.ecommerce.dto.request.CategoryUpdateRequest;
import com.minhbui.ecommerce.dto.response.CategoryResponse;
import com.minhbui.ecommerce.model.Category;
import com.minhbui.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/category")
    ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryCreationRequest request) {
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(categoryService.createCategory(request));
        return apiResponse;
    }

    //method for shopOwner
    @GetMapping("/category")
    List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategoriesOfShop();
    }

    //method for user
    @GetMapping("/categoryForUser/{shopId}")
    List<CategoryResponse> getAllCategoriesForUser(@PathVariable("shopId") Long shopId) {
        return categoryService.getAllCategoriesOfShopWithShopId(shopId);
    }


    @GetMapping("/category/{id}")
    CategoryResponse getCategoryById(@PathVariable("id") Long id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/category/{id}")
    CategoryResponse updateCategory(@PathVariable("id") Long id, @RequestBody CategoryUpdateRequest request) {
        return categoryService.updateCategory(request, id);
    }

    @DeleteMapping("/category/{id}")
    ApiResponse<String> deleteCategoryById(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Category deleted")
                .build();
    }
}

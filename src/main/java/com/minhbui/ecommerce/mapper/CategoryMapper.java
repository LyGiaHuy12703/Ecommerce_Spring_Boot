package com.minhbui.ecommerce.mapper;

import com.minhbui.ecommerce.dto.request.CategoryCreationRequest;
import com.minhbui.ecommerce.dto.request.CategoryUpdateRequest;
import com.minhbui.ecommerce.dto.response.CategoryResponse;
import com.minhbui.ecommerce.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreationRequest request);
    //map categoryUpdateRequest vào category
    void updateCategory(@MappingTarget Category category, CategoryUpdateRequest request);
    CategoryResponse toCateResponse(Category category);
    List<CategoryResponse> toCategoryResponseList(List<Category> categoryList);
}

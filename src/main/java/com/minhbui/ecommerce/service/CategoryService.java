package com.minhbui.ecommerce.service;

import com.minhbui.ecommerce.dto.request.CategoryCreationRequest;
import com.minhbui.ecommerce.dto.request.CategoryUpdateRequest;
import com.minhbui.ecommerce.dto.response.CategoryResponse;
import com.minhbui.ecommerce.exception.AppCatchException;

import com.minhbui.ecommerce.exception.ErrorCode;
import com.minhbui.ecommerce.mapper.CategoryMapper;
import com.minhbui.ecommerce.model.Category;
import com.minhbui.ecommerce.model.Shop;
import com.minhbui.ecommerce.model.User;
import com.minhbui.ecommerce.repository.CategoryRepository;
import com.minhbui.ecommerce.repository.ShopRepository;
import com.minhbui.ecommerce.repository.UserRepository;
import lombok.AccessLevel;


import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
     CategoryRepository categoryRepository;
     CategoryMapper categoryMapper;
     UserRepository userRepository;
     ShopRepository shopRepository;

    @PreAuthorize("hasRole('SHOP')")
    public CategoryResponse createCategory(CategoryCreationRequest request) {
        //lấy email người dùng
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        if(owner == null) {
            throw new AppCatchException(ErrorCode.USER_NOT_FOUND);
        }

        Shop shop = shopRepository.findByOwner(owner)
                .orElseThrow(() -> new AppCatchException(ErrorCode.SHOP_NOT_FOUND));

        Category category = categoryMapper.toCategory(request);
        category.setShop(shop);
        Date currentDate = new Date();

        category.setCreatedAt(currentDate);

        category.setDisabled(false);

        return categoryMapper.toCateResponse(categoryRepository.save(category));
    }

    //method for shopOwner
    @PreAuthorize("hasRole('SHOP')")
    public List<CategoryResponse> getAllCategoriesOfShop() {

        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        if(owner == null) {
            throw new AppCatchException(ErrorCode.USER_NOT_FOUND);
        }

        Shop shop = shopRepository.findByOwner(owner).orElse(null);

        if(shop == null) {
            throw new AppCatchException(ErrorCode.SHOP_NOT_FOUND);
        }

        return categoryMapper.toCategoryResponseList(categoryRepository.findAllCateOfShopDisabled(shop));
    }

    //method for user
    @PreAuthorize("hasRole('USER')")
    public List<CategoryResponse> getAllCategoriesOfShopWithShopId(Long shopId) {

        Shop shop = shopRepository.findById(shopId).orElse(null);

        if(shop == null) {
            throw new AppCatchException(ErrorCode.SHOP_NOT_FOUND);
        }

        return categoryMapper.toCategoryResponseList(categoryRepository.findAllCateOfShopDisabled(shop));
    }

    @PreAuthorize("hasRole('USER')")
    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id).orElse(null);

        if (category.isDisabled()){
            throw new AppCatchException(ErrorCode.CATEGORY_DISABLE);
        }

        return categoryMapper.toCateResponse(category);
    }


    @PreAuthorize("hasRole('SHOP')")
    public CategoryResponse updateCategory(CategoryUpdateRequest request, Long id) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new AppCatchException(ErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getShop().getOwner().equals(owner)) {
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }
        if (category.isDisabled()){
            throw new AppCatchException(ErrorCode.CATEGORY_DISABLE);
        }

        categoryMapper.updateCategory(category, request);
        Date currentDate = new Date();

        category.setCreatedAt(currentDate);

        return categoryMapper.toCateResponse(categoryRepository.save(category));
    }
    @PreAuthorize("hasRole('SHOP')")
    public void deleteCategoryById(Long id) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        Category category = categoryRepository.findById(id).orElse(null);

        if (!category.getShop().getOwner().getId().equals(owner.getId())) {
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }
        category.setDisabled(true);
        categoryRepository.save(category);
    }
}

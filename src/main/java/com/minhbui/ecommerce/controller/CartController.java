package com.minhbui.ecommerce.controller;

import com.minhbui.ecommerce.dto.request.ApiResponse;
import com.minhbui.ecommerce.dto.request.CartItemRequest;
import com.minhbui.ecommerce.dto.response.CartItemResponse;
import com.minhbui.ecommerce.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartItemService cartItemService;

    @PostMapping("/add")
    ApiResponse<CartItemResponse> addItemToCart(@RequestBody CartItemRequest request) {
        ApiResponse<CartItemResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(cartItemService.addItemToCart(request));
        return apiResponse;
    }
    @GetMapping
    List<CartItemResponse> getCartItems() {
        return cartItemService.getCartItems();
    }

    @DeleteMapping("/item/{id}")
    ApiResponse<Object> addItemToCart(@PathVariable Long id) {
        cartItemService.clearItem(id);
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Đã xóa sản phẩm")
                .build();
    }
    @PutMapping("/update-quantity")
    ApiResponse<CartItemResponse> updateQuantity(@RequestBody CartItemRequest request) {
        ApiResponse<CartItemResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(cartItemService.updateQuantityItem(request));
        return apiResponse;
    }

    @DeleteMapping("/clear")
    ApiResponse<Object> clearCartItems() {
        cartItemService.clearAllItems();
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Đã xóa tất cả sản phẩm")
                .build();
    }
}

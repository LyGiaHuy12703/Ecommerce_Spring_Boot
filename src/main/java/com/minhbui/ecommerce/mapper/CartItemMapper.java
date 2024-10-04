package com.minhbui.ecommerce.mapper;

import com.minhbui.ecommerce.dto.response.CartItemResponse;
import com.minhbui.ecommerce.model.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    List<CartItemResponse> toResponseListCartItem(List<CartItem> cartItems);
    CartItemResponse toCartItemResponse(CartItem cartItem);
    CartItem toCartItem(CartItemResponse cartItemResponse);
}

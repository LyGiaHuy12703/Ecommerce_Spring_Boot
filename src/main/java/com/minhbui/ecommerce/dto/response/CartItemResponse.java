package com.minhbui.ecommerce.dto.response;

import com.minhbui.ecommerce.model.Cart;
import com.minhbui.ecommerce.model.ProductSkus;

public class CartItemResponse {
    Cart cart;
    ProductSkus productSku;
    Long totalPrice;
    Integer quantity;
}

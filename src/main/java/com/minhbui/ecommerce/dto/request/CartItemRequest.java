package com.minhbui.ecommerce.dto.request;

import com.minhbui.ecommerce.model.Cart;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemRequest {
    Cart cart;
    Long productSku;
    Long totalPrice;
    Integer quantity;
}

package com.minhbui.ecommerce.dto.response;

import com.minhbui.ecommerce.enums.OrderStatus;
import com.minhbui.ecommerce.model.Address;
import com.minhbui.ecommerce.model.OrderItem;
import com.minhbui.ecommerce.model.Shop;
import com.minhbui.ecommerce.model.User;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    Long id;
    Long totalPrice;
    OrderStatus orderStatus;
    Integer totalItem;
    Date createdAt;
    User customer;
    Shop shop;
    Address address;
    Set<OrderItem> orderItems;
}

package com.minhbui.ecommerce.dto.request;

import com.minhbui.ecommerce.enums.OrderStatus;
import com.minhbui.ecommerce.model.OrderItem;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    Long totalPrice;
    Set<OrderItem> orderItems;

    Integer totalItem;

    Long shopId;
    Long customerId;
    Long addressId;
}

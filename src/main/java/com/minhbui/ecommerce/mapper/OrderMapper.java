package com.minhbui.ecommerce.mapper;

import com.minhbui.ecommerce.dto.request.OrderRequest;
import com.minhbui.ecommerce.dto.response.OrderResponse;
import com.minhbui.ecommerce.model.Order;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(OrderRequest order);
    OrderResponse toOrderResponse(Order order);
    List<OrderResponse> toOrderResponseList(Set<Order> orders);
}

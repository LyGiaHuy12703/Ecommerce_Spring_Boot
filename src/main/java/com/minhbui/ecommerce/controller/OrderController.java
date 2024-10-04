package com.minhbui.ecommerce.controller;

import com.minhbui.ecommerce.dto.request.ApiResponse;
import com.minhbui.ecommerce.dto.request.OrderRequest;
import com.minhbui.ecommerce.dto.request.OrderUpdateStatusRequest;
import com.minhbui.ecommerce.dto.response.OrderResponse;
import com.minhbui.ecommerce.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;
    @PostMapping
    ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderService.createOrder(request));
        apiResponse.setMessage("Successfully created order");
        return apiResponse;
    }
    @PostMapping("/{id}/status")
    ApiResponse<String> updateStatus(
            @PathVariable("id") long id,
            @RequestBody OrderUpdateStatusRequest request
            ) {
        orderService.updateStatus(id, request);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Status updated");
        return apiResponse;
    }

    @PostMapping("/{id}/cancel")
    ApiResponse<String> cancelOrder(@PathVariable("id") long id) {
        orderService.cancelOrder(id);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Order cancelled");
        return apiResponse;
    }
//    @PostMapping("{id}/confirm")
//    ApiResponse<String> confirmOrder(@PathVariable("id") long id) {
//        orderService.completeOrder(id);
//        ApiResponse<String> apiResponse = new ApiResponse<>();
//        apiResponse.setMessage("Order confirmed");
//        return apiResponse;
//    }

    @GetMapping("/{status}/me")
    List<OrderResponse> getOrderStatus(@PathVariable("status") int status) {
        return orderService.getOrderByMe(status);
    }

    @GetMapping("/{status}/shop")
    List<OrderResponse> getOrderShop(@PathVariable("status") int status) {
        return orderService.getOrdersOfShopByStatus(status);
    }

}

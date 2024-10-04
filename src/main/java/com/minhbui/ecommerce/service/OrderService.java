package com.minhbui.ecommerce.service;

import com.minhbui.ecommerce.dto.request.OrderRequest;
import com.minhbui.ecommerce.dto.request.OrderUpdateStatusRequest;
import com.minhbui.ecommerce.dto.response.OrderResponse;
import com.minhbui.ecommerce.enums.OrderStatus;
import com.minhbui.ecommerce.exception.AppCatchException;
import com.minhbui.ecommerce.exception.ErrorCode;
import com.minhbui.ecommerce.mapper.OrderMapper;
import com.minhbui.ecommerce.model.*;
import com.minhbui.ecommerce.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    UserRepository userRepository;
    ShopRepository shopRepository;
    CartRepository cartRepository;
    OrderMapper orderMapper;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));

        Shop shop = shopRepository.findById(orderRequest.getShopId())
                .orElseThrow(() -> new AppCatchException(ErrorCode.SHOP_NOT_FOUND));

        Address address = user.getAddresses().stream()
                .filter(addr -> addr.getId().equals(orderRequest.getAddressId()))
                .findFirst().orElseThrow(() -> new AppCatchException(ErrorCode.ADDRESS_NOT_FOUND));

        Cart cart = cartRepository.findById(user.getId())
                .orElseThrow(() -> new AppCatchException(ErrorCode.CART_NOT_FOUND));
        if (cart.getCartItems().isEmpty()) {
            throw new AppCatchException(ErrorCode.CART_EMPTY);
        }

        //tính số lượng và giá trị trong giỏ hàng
        Set<CartItem> cartItems = cart.getCartItems();
        Long totalPrice = cartItems.stream().mapToLong(CartItem::getTotalPrice).sum();
        Integer totalItems = cartItems.stream().mapToInt(CartItem::getQuantity).sum();

        //tạo đối tượng order
        Order order = orderMapper.toOrder(orderRequest);

        Date currentDate = new Date();
        order.setCreatedAt(currentDate);
        order.setShop(shop);
        order.setAddress(address);
        order.setCustomer(user);
        order.setOrderStatus(OrderStatus.PENDING);

        //tạo các orderItem từ cartItem
        Set<OrderItem> orderItems = new HashSet<>();
        for(CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productSku(cartItem.getProductSku())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(totalPrice)
                    .build();
            orderItems.add(orderItem);
        }

        //lưu vào đơn hàng và các sản phẩm trong đơn hàng
        order.setOrderItems(orderItems);
        orderRepository.save(order);

        //xóa giỏ hàng sau khi cập nhật đơn hàng
        // Xóa các CartItem đã được đặt trong đơn hàng
        for (CartItem cartItem : cartItems) {
            // Kiểm tra xem CartItem có trong OrderItem không
            boolean isOrdered = orderItems.stream().anyMatch(orderItem -> orderItem.getProductSku().equals(cartItem.getProductSku()));
            if (isOrdered) {
                cart.removeCartItem(cartItem); // Giả định phương thức này xóa CartItem cụ thể
            }
        }

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setCustomer(user);
        orderResponse.setShop(shop);
        orderResponse.setAddress(address);
        orderResponse.setOrderStatus(OrderStatus.PENDING);
        orderResponse.setOrderItems(orderItems);
        orderResponse.setCreatedAt(new Date());

        return orderResponse;
    }

    /// update trạng thái đơn hàng
    @PreAuthorize("hasRole('SHOP')")
    public void updateStatus(Long id, OrderUpdateStatusRequest request) {
        if(request.getStatus() < OrderStatus.PENDING.getValue() || request.getStatus() > OrderStatus.CANCELLED.getValue()) {
            throw  new AppCatchException(ErrorCode.ORDER_STATUS_INVALID);
        }

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.ORDER_NOT_FOUND));

        if(request.getStatus() != OrderStatus.CANCELLED.getValue()) {
            if(order.getOrderStatus().getValue() > request.getStatus()) {
                throw new AppCatchException(ErrorCode.ORDER_STATUS_INVALID);
            }
        }

        order.setOrderStatus(OrderStatus.fromValue(request.getStatus()));
        orderRepository.save(order);
    }

    //hủy đơn
    @PreAuthorize("hasRole('USER')")
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.ORDER_NOT_FOUND));
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

//    //xasc nhận đã nhận hàng
//    @PreAuthorize("hasRole('USER')")
//    public void completeOrder(Long id) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        userRepository.findByEmail(email)
//                .orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));
//
//        Order order = orderRepository.findById(id)
//                .orElseThrow(() -> new AppCatchException(ErrorCode.ORDER_NOT_FOUND));
//
//        if(order.getOrderStatus() != OrderStatus.PENDING) {
//            throw new AppCatchException(ErrorCode.ORDER_STATUS_INVALID);
//        }
//        order.setOrderStatus(OrderStatus.);
//        orderRepository.save(order);
//
//    }
    //lấy tát cả đơn hàng có status
    @PreAuthorize("hasRole('USER')")
    public List<OrderResponse> getOrderByMe(int status){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));

        Set<Order> orders = owner.getOrders();

        var orderFilter = orders.stream().filter(order -> order.getOrderStatus().getValue() == status).collect(Collectors.toSet());
        return orderMapper.toOrderResponseList(orderFilter);
    }

    //lấy tất cả đơn hàng của shop theo trạng thái
    @PreAuthorize("hasRole('SHOP')")
    public List<OrderResponse> getOrdersOfShopByStatus(int status) {
        // neu status -1 thi lay het
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppCatchException(ErrorCode.USER_NOT_FOUND));

        Shop shop = shopRepository.findByOwner(user)
                .orElseThrow(() -> new AppCatchException(ErrorCode.SHOP_NOT_FOUND));

        Set<Order> orders = shop.getOrders();

        var ordersFilter = orders;
        if(status>=0){
            log.info("STATUS "+status);
            ordersFilter = orders.stream().filter(order ->
                    order.getOrderStatus().getValue() == status)
                    .collect(Collectors.toSet());
        }
        return orderMapper.toOrderResponseList(ordersFilter);
    }

}

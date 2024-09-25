package com.minhbui.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.minhbui.ecommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    Long id;

    Long totalPrice;

    OrderStatus orderStatus;

    Integer totalItem;

    Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @JsonBackReference
    Shop shop;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<OrderItem> orderItems = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    Address address;
}

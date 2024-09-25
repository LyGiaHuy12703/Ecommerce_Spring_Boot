package com.minhbui.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Cart {
    @Id
    @GeneratedValue
    Long id;

    Long total;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    User customer;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<CartItem> cartItems = new HashSet<>();
}

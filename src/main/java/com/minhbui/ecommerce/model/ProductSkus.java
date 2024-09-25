package com.minhbui.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class ProductSkus {
    @Id
    @GeneratedValue
    Long id;

    Long price;
    Integer stockQuantity;
    Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    Product product;

    @OneToMany(mappedBy = "productSku",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<ProductAttributes> productAttributes = new HashSet<>();

    @OneToMany(mappedBy = "productSku",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<CartItem> cartItems = new HashSet<>();

    @OneToMany(mappedBy = "productSku",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<OrderItem> orderItems = new HashSet<>();
}

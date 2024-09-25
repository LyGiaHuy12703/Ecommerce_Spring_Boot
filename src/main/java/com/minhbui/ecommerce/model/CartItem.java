package com.minhbui.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CartItem {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_skus")
    ProductSkus productSku;

    Long totalPrice;
    Integer quantity;
}

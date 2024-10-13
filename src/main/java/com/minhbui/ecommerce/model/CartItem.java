package com.minhbui.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    ProductSkus productSku;

    Long totalPrice;
    Integer quantity;

    public CartItem(Cart cart, ProductSkus productSkus, int i, int i1) {
        this.cart = cart;
        this.productSku = productSkus;
        this.quantity = i;
        this.totalPrice = productSkus.getPrice() * i;
    }

}

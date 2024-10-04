package com.minhbui.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Iterator;
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

    public int getTotalItemsInCart() {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity) // Lấy số lượng của mỗi CartItem
                .sum(); // Tính tổng số lượng
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
    }

    public void removeAllCartItems() {
        Iterator<CartItem> iterator = cartItems.iterator();
        while(iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            cartItem.setCart(null);
            iterator.remove();
        }
    }
}

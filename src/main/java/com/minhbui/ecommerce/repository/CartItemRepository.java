package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.model.Cart;
import com.minhbui.ecommerce.model.CartItem;
import com.minhbui.ecommerce.model.ProductSkus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
    // Tìm kiếm CartItem bằng cartId và productSkusId nếu khong tim thấy tr về empty
    Optional<CartItem> findByCartAndProductSku(Cart cart, ProductSkus productSku);

}

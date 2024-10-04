package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.model.Shop;
import com.minhbui.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByOwner(User user);



}

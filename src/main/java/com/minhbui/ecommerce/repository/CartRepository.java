package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.model.Cart;
import com.minhbui.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}

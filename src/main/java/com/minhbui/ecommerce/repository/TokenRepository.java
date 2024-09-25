package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.model.Cart;
import com.minhbui.ecommerce.model.Token;
import com.minhbui.ecommerce.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUser(User user);

    @Transactional
    void deleteTokenByUser(User user);

    Optional<Token> findTokenByRefreshToken(String token);
}

package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.model.Category;
import com.minhbui.ecommerce.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByShop(Shop shop);

    @Query("SELECT c FROM Category c join c.shop s WHERE c.disabled = false and c.shop=?1 and c.shop.disabled=false ")
    List<Category> findAllCateOfShopDisabled(Shop shop);
}

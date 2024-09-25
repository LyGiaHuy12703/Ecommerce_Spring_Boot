package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.model.Category;
import com.minhbui.ecommerce.model.Product;
import com.minhbui.ecommerce.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select p from Product p where p.shop = ?1")
    public List<Product> findByShop(Shop shop);

    //lấy tất cả sản phẩm của shop với category khác disable
    @Query("SELECT p FROM Product p JOIN p.category c WHERE p.shop = ?1 AND c.disabled = false and p.disabled=false")
    List<Product> findProductsByShopWithDisabledCategory(Shop shop);

    //lấy tất cả sản phẩm của category với category khác disable
    @Query("select p from Product p JOIN p.category c where p.category = ?1 and p.disabled = false and c.disabled = false")
    List<Product> findProductsByCategory(Category category);
}



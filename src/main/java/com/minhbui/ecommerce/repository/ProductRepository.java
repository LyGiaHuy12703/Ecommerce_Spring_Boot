package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.dto.response.ProductResponse;
import com.minhbui.ecommerce.model.Category;
import com.minhbui.ecommerce.model.Product;
import com.minhbui.ecommerce.model.Shop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select p from Product p where p.shop = ?1")
    List<Product> findByShop(Shop shop);

    //lấy tất cả sản phẩm không disable với phân trang
    Page<Product> findALlByDisabledFalse(Pageable pageable);

    //lấy tất cả sản phẩm phân trang theo shop
//    Page<ProductResponse>

    //lấy tất cả sản phẩm của shop với category khác disable
    @Query("SELECT p FROM Product p JOIN p.category c WHERE p.shop = ?1 AND c.disabled = false and p.disabled=false")
    List<Product> findProductsByShopWithDisabledCategory(Shop shop, Pageable pageable);

    //lấy tất cả sản phẩm của category với category khác disable
    @Query("select p from Product p JOIN p.category c where p.category = ?1 and p.disabled = false and c.disabled = false")
    List<Product> findProductsByCategory(Category category, Pageable pageable);

    // lấy tất cả sản phẩm với disable = false
    @Query("select p from Product p where p.disabled=false")
    List<Product> findAllProducts();
    // Sắp xếp giá từ thấp đến cao dựa trên maxPrice trong Product
    @Query("SELECT p FROM Product p WHERE p.shop = :shop ORDER BY p.maxPrice ASC")
    List<Product> findByShopOrderByMinPriceAsc(@Param("shop") Shop shop, Pageable pageable);

    // Sắp xếp giá từ cao xuống thấp dựa trên maxPrice trong Product
    @Query("SELECT p FROM Product p WHERE p.shop = :shop ORDER BY p.maxPrice DESC")
    List<Product> findByShopOrderByMinPriceDesc(@Param("shop") Shop shop, Pageable pageable);

    // Tìm kiếm theo tên sản phẩm (case-insensitive)
    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    // Tìm kiếm theo tên và sắp xếp theo giá từ thấp đến cao
    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY p.maxPrice ASC")
    Page<Product> findByNameOrderByPriceAsc(@Param("name") String name, Pageable pageable);

    // Tìm kiếm theo tên và sắp xếp theo giá từ thấp đến cao
    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY p.maxPrice DESC ")
    Page<Product> findByNameOrderByPriceDesc(@Param("name") String name, Pageable pageable);

    // Tìm kiếm theo shop và giá sản phẩm
    @Query("SELECT p FROM Product p WHERE p.shop = :shop AND p.maxPrice >= :minPrice AND p.maxPrice <= :maxPrice")
    Page<Product> findByShopAndPriceRange(@Param("shop") Shop shop, @Param("minPrice") Long minPrice, @Param("maxPrice") Long maxPrice, Pageable pageable);

}



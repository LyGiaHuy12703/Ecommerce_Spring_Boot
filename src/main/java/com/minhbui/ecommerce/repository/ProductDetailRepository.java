package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

}

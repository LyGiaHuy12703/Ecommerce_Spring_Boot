package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.model.ProductSkus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSkusRepository extends JpaRepository<ProductSkus, Long> {

}

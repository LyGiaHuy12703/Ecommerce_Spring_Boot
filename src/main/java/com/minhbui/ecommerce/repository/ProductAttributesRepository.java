package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.model.ProductAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributesRepository extends JpaRepository<ProductAttributes, Long> {
}

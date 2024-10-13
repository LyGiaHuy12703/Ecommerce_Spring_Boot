package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.model.ProductAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductAttributesRepository extends JpaRepository<ProductAttributes, Long> {
    Boolean existsByType(String type);
    Boolean existsByValue(String value);

    @Query("SELECT pa FROM ProductAttributes pa WHERE pa.type = :type AND pa.value = :value")
    Set<ProductAttributes> findProductAttributesByTypeAndValue(@Param("type") String type, @Param("value") String value);

}

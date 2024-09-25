package com.minhbui.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_detail")
public class ProductDetail {
    @Id
    @GeneratedValue
    Long id;

    String field;
    String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
            @JsonBackReference
    Product product;

    
}

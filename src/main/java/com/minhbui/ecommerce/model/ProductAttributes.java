package com.minhbui.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ProductAttributes {
    @Id
    @GeneratedValue
    Long id;

    String type;
    String value;

    Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_sku")
    ProductSkus productSku;

}

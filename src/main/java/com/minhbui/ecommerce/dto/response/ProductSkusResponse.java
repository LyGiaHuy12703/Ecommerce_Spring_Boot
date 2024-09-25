package com.minhbui.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.minhbui.ecommerce.model.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSkusResponse {
    Long id;
    Long price;
    Integer stockQuantity;
    Date createdAt;
    Product product;
}

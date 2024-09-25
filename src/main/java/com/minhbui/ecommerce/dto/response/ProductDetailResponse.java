package com.minhbui.ecommerce.dto.response;

import com.minhbui.ecommerce.model.Product;
import com.minhbui.ecommerce.model.ProductDetail;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
    Long id;
    String field;
    String value;
    Product product;
}

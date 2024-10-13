package com.minhbui.ecommerce.dto.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductSkusAndAttributeRequest {
    String type;
    String value;
    Long price;
    Integer stockQuantity;
    Long productId;
}

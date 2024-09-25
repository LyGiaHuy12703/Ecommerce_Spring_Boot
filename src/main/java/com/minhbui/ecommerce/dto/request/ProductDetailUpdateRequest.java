package com.minhbui.ecommerce.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailUpdateRequest {
    String field;
    String value;
    Long productId;
}

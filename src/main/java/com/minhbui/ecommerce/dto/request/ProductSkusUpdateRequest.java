package com.minhbui.ecommerce.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSkusUpdateRequest {
    Long price;
    Integer stockQuantity;
    Long productId;
}

package com.minhbui.ecommerce.dto.request;

import com.minhbui.ecommerce.model.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSkusCreationRequest {
    Long price;
    Integer stockQuantity;
    Long productId;
}

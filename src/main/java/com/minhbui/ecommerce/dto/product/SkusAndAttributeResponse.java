package com.minhbui.ecommerce.dto.product;

import com.minhbui.ecommerce.model.Product;
import com.minhbui.ecommerce.model.ProductAttributes;
import com.minhbui.ecommerce.model.ProductSkus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SkusAndAttributeResponse {
    Product product;
}

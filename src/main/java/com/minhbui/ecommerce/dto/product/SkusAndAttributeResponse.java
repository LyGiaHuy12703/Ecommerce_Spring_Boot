package com.minhbui.ecommerce.dto.product;

import com.minhbui.ecommerce.model.ProductAttributes;
import com.minhbui.ecommerce.model.ProductSkus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SkusAndAttributeResponse {
//    Set<ProductAttributes> productAttributes;
    ProductSkus productSkus;
    ProductAttributes productAttributes;
}

package com.minhbui.ecommerce.dto.request;

import com.minhbui.ecommerce.model.ProductSkus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAttributesCreateRequest {
    Long id;
    String type;
    String value;
    ProductSkus productSku;
}

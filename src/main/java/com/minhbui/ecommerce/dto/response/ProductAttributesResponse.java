package com.minhbui.ecommerce.dto.response;

import com.minhbui.ecommerce.model.ProductSkus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAttributesResponse {
    Long id;

    String type;
    String value;

    Date createdAt;
    ProductSkus productSku;
}

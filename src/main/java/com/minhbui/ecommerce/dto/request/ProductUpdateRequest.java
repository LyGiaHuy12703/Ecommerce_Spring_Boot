package com.minhbui.ecommerce.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String title;
    String description;
    Integer soldQuantity;
    Integer minPrice;
    Integer maxPrice;
    Long categoryId;

}

package com.minhbui.ecommerce.dto.request;

import com.minhbui.ecommerce.model.Category;
import com.minhbui.ecommerce.model.Event;
import com.minhbui.ecommerce.model.ProductDetail;
import com.minhbui.ecommerce.model.Shop;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreationRequest {
    String title;
    String description;
    Integer soldQuantity;
    Date createdAt;
    Integer minPrice;
    Integer maxPrice;
    Long shopId;
    Long categoryId;

}

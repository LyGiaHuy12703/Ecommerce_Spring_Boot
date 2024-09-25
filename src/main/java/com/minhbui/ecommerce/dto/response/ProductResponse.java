package com.minhbui.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.*;
import com.minhbui.ecommerce.model.Category;
import com.minhbui.ecommerce.model.Event;
import com.minhbui.ecommerce.model.ProductDetail;
import com.minhbui.ecommerce.model.Shop;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    Long id;
    String title;
    String description;
    Integer soldQuantity;
    Date createdAt;
    Long minPrice;
    Long maxPrice;
    Shop shop;
    Category category;
    Set<String> images;
    Set<ProductDetail> productDetail;
    Event event;
}

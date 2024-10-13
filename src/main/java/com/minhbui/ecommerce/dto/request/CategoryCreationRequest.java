package com.minhbui.ecommerce.dto.request;

import com.minhbui.ecommerce.model.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CategoryCreationRequest {
    String name;
    String description;

}

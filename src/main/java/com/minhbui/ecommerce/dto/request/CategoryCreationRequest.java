package com.minhbui.ecommerce.dto.request;

import com.minhbui.ecommerce.model.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CategoryCreationRequest {
    Long id;
    String name;
    String description;
    Date createdAt;


//    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
//    Set<Product> products = new HashSet<>();
}

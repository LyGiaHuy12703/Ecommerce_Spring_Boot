package com.minhbui.ecommerce.dto.request;

import com.minhbui.ecommerce.model.Product;
import com.minhbui.ecommerce.model.Shop;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventCreationRequest {
    String title;
    String description;
    Integer discount;
    Long shopId;

}

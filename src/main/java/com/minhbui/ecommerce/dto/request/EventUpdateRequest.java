package com.minhbui.ecommerce.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateRequest {
    String title;
    String description;
    Integer discount;
    Long shopId;
}

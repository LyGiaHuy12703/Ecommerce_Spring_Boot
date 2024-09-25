package com.minhbui.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopResponse {

    Long shopId;
    Set<String> images = new HashSet<>();
    UserResponse owner;

    String name;
    String avatar;
    String description;
    Date created;
    TokenResponse token;
}

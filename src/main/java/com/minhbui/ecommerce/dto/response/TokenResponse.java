package com.minhbui.ecommerce.dto.response;

import lombok.Builder;

@Builder
public record TokenResponse(
        String access_token,
        String refresh_token
) {
}

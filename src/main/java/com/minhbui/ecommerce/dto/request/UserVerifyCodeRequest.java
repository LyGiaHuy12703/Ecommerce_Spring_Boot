package com.minhbui.ecommerce.dto.request;

public record UserVerifyCodeRequest(
        String code,
        Long userId
) {
}

package com.minhbui.ecommerce.dto.request;

public record SignInRequest(
        String email,
        String password
) {
}

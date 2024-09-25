package com.minhbui.ecommerce.dto.request;

public record SignUpRequest(
        String email,
        String firstName,
        String lastName,
        String password
) {
}

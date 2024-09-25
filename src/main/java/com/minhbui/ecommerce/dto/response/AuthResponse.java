package com.minhbui.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse{
    Long id;
    String email;
    String firstName;
    String lastName;
    String role;
    TokenResponse token;
}

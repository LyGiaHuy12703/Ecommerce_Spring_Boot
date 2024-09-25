package com.minhbui.ecommerce.mapper;

import com.minhbui.ecommerce.dto.request.SignUpRequest;
import com.minhbui.ecommerce.dto.response.AuthResponse;
import com.minhbui.ecommerce.dto.response.UserResponse;
import com.minhbui.ecommerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password",ignore = true)
    User toUser(SignUpRequest request);

    UserResponse userToUserResponse(User user);

    @Mapping(target = "role",ignore = true)
    @Mapping(target = "token",ignore = true)
    AuthResponse userToAuthResponse(User user);
}

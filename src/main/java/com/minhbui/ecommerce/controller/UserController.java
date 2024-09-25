package com.minhbui.ecommerce.controller;

import com.minhbui.ecommerce.core.ResponseSuccess;
import com.minhbui.ecommerce.dto.request.RegisterShopRequest;
import com.minhbui.ecommerce.service.AuthenticateService;
import com.minhbui.ecommerce.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @PostMapping("/register-shop")
    public ResponseSuccess registerShop(
            @ModelAttribute RegisterShopRequest request,
            @RequestParam List<MultipartFile> files,
            @RequestParam MultipartFile avatar
    ) throws IOException {
        var result = userService.registerShop(request,files,avatar);
        return ResponseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Register Shop Success")
                .metadata(result)
                .build();
    }

}

package com.minhbui.ecommerce.controller;

import com.minhbui.ecommerce.core.ResponseSuccess;
import com.minhbui.ecommerce.dto.request.*;
import com.minhbui.ecommerce.service.AuthenticateService;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthenticateController {

    AuthenticateService authenticateService;

    @PostMapping("/signup")
    public ResponseSuccess signup(
            @RequestBody SignUpRequest request
    ) throws MessagingException {
        var result = authenticateService.signup(request);
        return ResponseSuccess.builder()
                .code(HttpStatus.CREATED.value())
                .message("Please verify code in your email")
                .metadata(result)
                .build();
    }

    @PostMapping("/verify")
    public ResponseSuccess verify(
            @RequestBody UserVerifyCodeRequest request
    )  {
        authenticateService.verifyCode(request);
        return ResponseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Verify Email Success")
                .build();
    }

    @PostMapping("/resend")
    public ResponseSuccess resendVerifyCode(
            @RequestBody ResendCodeRequest request
    ) throws MessagingException {
        authenticateService.resendVerifyCode(request);
        return ResponseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Resend verify code Success")
                .build();
    }


    @PostMapping("/signin")
    public ResponseSuccess signin(
            @RequestBody SignInRequest request
    ) {
        var result = authenticateService.signin(request);
        return ResponseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Verify Email Success")
                .metadata(result)
                .build();
    }

    @PostMapping("/refresh-token")
    public ResponseSuccess refreshToken(
            @RequestBody RefreshTokenRequest request
    ) throws ParseException, JOSEException {
        var result = authenticateService.refreshToken(request);
        return ResponseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Refresh Token Success")
                .metadata(result)
                .build();
    }

    @PostMapping("/logout")
    public ResponseSuccess logout() {
        var msg = authenticateService.logoutUser();
        return ResponseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message(msg)
                .build();
    }
}

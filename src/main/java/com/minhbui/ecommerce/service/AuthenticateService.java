package com.minhbui.ecommerce.service;


import com.minhbui.ecommerce.dto.DataMailDTO;
import com.minhbui.ecommerce.dto.request.*;
import com.minhbui.ecommerce.dto.response.AuthResponse;
import com.minhbui.ecommerce.dto.response.TokenResponse;
import com.minhbui.ecommerce.dto.response.UserResponse;
import com.minhbui.ecommerce.enums.ErrorEnum;
import com.minhbui.ecommerce.enums.RoleEnum;
import com.minhbui.ecommerce.exception.AppException;
import com.minhbui.ecommerce.mapper.UserMapper;
import com.minhbui.ecommerce.model.Cart;
import com.minhbui.ecommerce.model.Role;
import com.minhbui.ecommerce.model.Token;
import com.minhbui.ecommerce.model.User;
import com.minhbui.ecommerce.repository.CartRepository;
import com.minhbui.ecommerce.repository.RoleRepository;
import com.minhbui.ecommerce.repository.TokenRepository;
import com.minhbui.ecommerce.repository.UserRepository;
import com.minhbui.ecommerce.utils.AuthUtil;
import com.minhbui.ecommerce.utils.Const;
import com.minhbui.ecommerce.utils.Helper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final UserRepository userRepository;
    private final Helper helper;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MailService mailService;
    private final CartRepository cartRepository;
    private final AuthUtil authUtil;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;

    @Value("${jwt.secret.access_token}")
    private String accessTokenSecret;

    @Value("${jwt.secret.refresh_token}")
    private String refreshTokenSecret;

    private int expireAccessToken = 1;

    private int expireRefreshToken = 7;

    @Transactional
    public UserResponse signup(
            SignUpRequest signUpRequest
    ) throws MessagingException {
        User foundUser = userRepository.findByEmail(signUpRequest.email()).orElse(null);
        if(foundUser != null) {
            throw new AppException(ErrorEnum.EMAIL_EXISTS);
        }
        String verifyCode = helper.generateTempPwd(6);
        String passwordEncoded = passwordEncoder.encode(signUpRequest.password());


        //lưu người dùng
        User user = userMapper.toUser(signUpRequest);
        user.setRoles(new HashSet<>());

        Role foundRole = roleRepository.findByRoleName(RoleEnum.ROLE_SHOP).orElse(null);
        if(foundRole != null) {
            user.addRole(foundRole);
        }else {
            user.addRole(Role.builder()
                    .name(RoleEnum.ROLE_USER)
                    .users(new HashSet<>())
                    .build());
        }


        user.setPassword(passwordEncoded);
        user.setVerificationCode(verifyCode);
        user.setVerificationExpiry(LocalDateTime.now().plusHours(24));//thoi gian het han code 24h
        user.setCreatedAt(new Date());
        user.setVerified(false);

        User newUser = userRepository.save(user);

        //Gui mail
        Map<String,Object> props = new HashMap<>();
        props.put("firstName",signUpRequest.firstName());
        props.put("lastName",signUpRequest.lastName());
        props.put("code",verifyCode);

        DataMailDTO dataMailDTO = DataMailDTO.builder()
                .subject(Const.CLIENT_REGISTER)
                .to(signUpRequest.email())
                .props(props)
                .build();
        mailService.sendHtmlMail(dataMailDTO);



        return userMapper.userToUserResponse(newUser);
    }

    public void verifyCode(
            UserVerifyCodeRequest request
    ) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        if(!user.getVerificationCode().equals(request.code())) {
            throw new AppException(ErrorEnum.VERIFICATION_FAILED);
        }
        if(LocalDateTime.now().isAfter(user.getVerificationExpiry())) {
            throw new AppException(ErrorEnum.VERIFICATION_EXPIRED);
        }

        Cart cart = Cart.builder()
                .customer(user)
                .total(0L)
                .build();
        cartRepository.save(cart);

        user.setVerified(true);
        user.setVerificationExpiry(null);
        user.setVerificationCode(null);


        userRepository.save(user);

    }

    public void resendVerifyCode(
            ResendCodeRequest request
    ) throws MessagingException {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        if(user.getVerified()) {
            throw new AppException(ErrorEnum.ACCOUNT_VERIFIED);
        }

        String verifyCode = helper.generateTempPwd(6);
        user.setVerificationCode(verifyCode);
        user.setVerificationExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        //Gui mail
        Map<String,Object> props = new HashMap<>();
        props.put("firstName",user.getFirstName());
        props.put("lastName",user.getLastName());
        props.put("code",verifyCode);

        DataMailDTO dataMailDTO = DataMailDTO.builder()
                .subject(Const.CLIENT_REGISTER)
                .to(user.getEmail())
                .props(props)
                .build();
        mailService.sendHtmlMail(dataMailDTO);
    }


    public Object signin(
            SignInRequest request
    ) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AppException(ErrorEnum.LOGIN_FAILED));

        var isMatched = passwordEncoder.matches(request.password(), user.getPassword());
        if(!isMatched) {
            throw new AppException(ErrorEnum.LOGIN_FAILED);
        }
        if(!user.getVerified()) {
            throw new AppException(ErrorEnum.ACCOUNT_NOT_VERIFIED);
        }

        //generate token
        String accessToken = authUtil.generateToken(user,expireAccessToken,accessTokenSecret,null);
        String refreshToken = authUtil.generateToken(user,expireRefreshToken,refreshTokenSecret,null);

        Token token = tokenRepository.findByUser(user).orElse(null);
        if(token == null) {
            tokenRepository.save(Token.builder()
                    .user(user)
                    .refreshToken(refreshToken)
                    .createdAt(new Date())
                    .build());
        }else {
            token.setRefreshToken(refreshToken);
            tokenRepository.save(token);
        }

        TokenResponse tokenResponse = TokenResponse.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .build();

        AuthResponse authResponse = userMapper.userToAuthResponse(user);
        authResponse.setToken(tokenResponse);
        authResponse.setRole(authUtil.builderRole(user));
        return authResponse;
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        String token = request.refresh_token();
        SignedJWT signedJWT = authUtil.verifyToken(token,refreshTokenSecret);
        //kiểm tra token trong db
        Token refreshTokenEntity = tokenRepository.findTokenByRefreshToken(token).orElse(null);
        if(refreshTokenEntity == null) {
            throw new AppException(ErrorEnum.UNAUTHENTICATED);
        }
        User user = userRepository.findById(refreshTokenEntity.getUser().getId()).orElse(null);
        if(user == null) {
            throw new AppException(ErrorEnum.USER_NOT_FOUND);
        }
        Date expireTimeOfRefreshToken = signedJWT.getJWTClaimsSet().getExpirationTime();
        String accessToken = authUtil.generateToken(user,expireAccessToken,accessTokenSecret,null);
        String refreshToken = authUtil.generateToken(user,expireRefreshToken,refreshTokenSecret,expireTimeOfRefreshToken);

        //cập nhật token trong db
        refreshTokenEntity.setRefreshToken(refreshToken);
        tokenRepository.save(refreshTokenEntity);
        TokenResponse tokenResponse =TokenResponse.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .build();
        return tokenResponse;
    }

    public String logoutUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        //xóa token trong db
        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null) {
            throw new  AppException(ErrorEnum.USER_NOT_FOUND);
        }
        tokenRepository.deleteTokenByUser(user);
        return "Logout successfully";
    }
}

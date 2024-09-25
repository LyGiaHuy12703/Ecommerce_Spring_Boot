package com.minhbui.ecommerce.service;

import com.cloudinary.utils.ObjectUtils;
import com.minhbui.ecommerce.config.CloudinaryConfig;
import com.minhbui.ecommerce.dto.request.RegisterShopRequest;
import com.minhbui.ecommerce.dto.response.ShopResponse;
import com.minhbui.ecommerce.dto.response.TokenResponse;
import com.minhbui.ecommerce.enums.ErrorEnum;
import com.minhbui.ecommerce.enums.RoleEnum;
import com.minhbui.ecommerce.exception.AppException;
import com.minhbui.ecommerce.mapper.ShopMapper;
import com.minhbui.ecommerce.model.Role;
import com.minhbui.ecommerce.model.Shop;
import com.minhbui.ecommerce.model.Token;
import com.minhbui.ecommerce.model.User;
import com.minhbui.ecommerce.repository.RoleRepository;
import com.minhbui.ecommerce.repository.ShopRepository;
import com.minhbui.ecommerce.repository.TokenRepository;
import com.minhbui.ecommerce.repository.UserRepository;
import com.minhbui.ecommerce.utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final CloudinaryConfig cloudinary;
    private final ShopMapper shopMapper;
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
    public ShopResponse registerShop(
        RegisterShopRequest registerShopRequest,
        List<MultipartFile> files,
        MultipartFile avatar
    ) throws IOException {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        Shop foundShop = shopRepository.findByOwner(user).orElse(null);
        if(foundShop != null) {
            throw new AppException(ErrorEnum.USER_REGISTERED_SHOP);
        }

        Set<String> imagesUrl = uploadMultiImg(files, String.valueOf(user.getId()));

        String avatarUrl = uploadImg(avatar, String.valueOf(user.getId()));

        Shop shop = Shop.builder()
                .owner(user)
                .images(imagesUrl)
                .avatar(avatarUrl)
                .name(registerShopRequest.getName())
                .description(registerShopRequest.getDescription())
                .disabled(false)
                .createdAt(new Date())
                .build();
        var newShop = shopRepository.save(shop);

        Role foundRole = roleRepository.findByRoleName(RoleEnum.ROLE_SHOP).orElse(null);
        if(foundRole != null) {
            user.addRole(foundRole);
        }else {
            user.addRole(Role.builder()
                    .name(RoleEnum.ROLE_SHOP)
                    .users(new HashSet<>())
                    .build());
        }

        User newUser = userRepository.save(user);
        //generate token
        String accessToken = authUtil.generateToken(newUser,expireAccessToken,accessTokenSecret,null);
        String refreshToken = authUtil.generateToken(newUser,expireRefreshToken,refreshTokenSecret,null);

        Token token = tokenRepository.findByUser(newUser).orElse(null);
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

        ShopResponse shopResponse = shopMapper.toShopResponse(newShop);
        shopResponse.setShopId(shop.getId());
        shopResponse.setToken(tokenResponse);

        return shopResponse;
    }

    private Set<String> uploadMultiImg(List<MultipartFile> files, String unique) throws IOException {
        Set<String> urlList = new HashSet<>();
        for (MultipartFile file : files) {
            var result = cloudinary.cloudinary().uploader()
                    .upload(file.getBytes(), ObjectUtils.asMap(
                            "folder", "ecommerce/shop_owner_"+unique
                    ));
            urlList.add((String) result.get("secure_url"));;
        }
        return urlList;
    }

    private String uploadImg(MultipartFile file, String unique) throws IOException {
        var result = cloudinary.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", "ecommerce/shop_owner_"+unique
        ));
        return (String) result.get("secure_url");
    }
}

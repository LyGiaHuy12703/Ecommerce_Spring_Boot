package com.minhbui.ecommerce.utils;

import com.minhbui.ecommerce.enums.ErrorEnum;
import com.minhbui.ecommerce.enums.RoleEnum;
import com.minhbui.ecommerce.exception.AppException;
import com.minhbui.ecommerce.model.Role;
import com.minhbui.ecommerce.model.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;

@Slf4j
@Service
public class AuthUtil {
    public String generateToken(
            User user,
            int expireDay,
            String secretKey,
            @Nullable Date expireTime
    ) {
        Date expirationTimeVar = expireTime == null ? new Date(
                Instant.now().plus(expireDay, ChronoUnit.DAYS).toEpochMilli()
        ) : expireTime;
        JWSHeader jwtHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new  JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("ecommerce.com")
                .issueTime(new Date())
                .expirationTime(expirationTimeVar)
                .claim("scope",builderRole(user))
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwtHeader, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
            return jwsObject.serialize();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String builderRole(User user) {
        StringJoiner roleStr = new StringJoiner(" ");
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {

            roleStr.add(role.getName().toString());
        }
        return roleStr.toString();
    }


    public SignedJWT verifyToken(String token, String secretKey) throws ParseException, JOSEException {
        var verifier = new MACVerifier(secretKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        var verify = signedJWT.verify(verifier);

        var expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if(!verify) {
            throw new AppException(ErrorEnum.UNAUTHENTICATED);
        }
        if(!expireTime.after(new Date())) {
            throw new AppException(ErrorEnum.TOKEN_EXPIRE);
        }
        return signedJWT;

    }
}

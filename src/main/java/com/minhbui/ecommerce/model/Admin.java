package com.minhbui.ecommerce.model;

import com.minhbui.ecommerce.enums.RoleEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Admin {
    @Id
    @GeneratedValue
    Long id;

    String username;
    String password;
    RoleEnum roleEnum = RoleEnum.ROLE_ADMIN;
}

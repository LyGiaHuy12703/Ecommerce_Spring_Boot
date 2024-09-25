package com.minhbui.ecommerce.model;

import com.minhbui.ecommerce.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Role {

    @Id
    @GeneratedValue
    Long id;

    @Enumerated(EnumType.STRING)
    RoleEnum name;

    @ManyToMany(mappedBy = "roles")
    Set<User> users = new HashSet<>();
}

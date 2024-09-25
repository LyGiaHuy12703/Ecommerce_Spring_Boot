package com.minhbui.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minhbui.ecommerce.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue
    Long id;

    @Column(unique = true)
    String email;

    @JsonIgnore
    String password;

    String firstName;
    String lastName;

    Date createdAt;

    Boolean verified = false;

    String verificationCode;
    LocalDateTime verificationExpiry;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
//    @JsonIgnore
    Set<Role> roles = new HashSet<>();


    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<Address> addresses;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<Order> orders;

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }
}

package com.minhbui.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Token {

    @Id
    @GeneratedValue
    Long id;

    @Column(length = 1024)
    String refreshToken;

    Date createdAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;


}

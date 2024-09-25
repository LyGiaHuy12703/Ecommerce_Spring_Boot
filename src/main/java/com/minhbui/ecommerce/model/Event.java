package com.minhbui.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Event {
    @Id
    @GeneratedValue
    Long id;

    String title;
    String description;
    Integer discount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
            @JsonBackReference
    Shop shop;

    @OneToMany(mappedBy = "event")
    @JsonManagedReference
    Set<Product> products = new HashSet<>();
    boolean disabled = false;
}

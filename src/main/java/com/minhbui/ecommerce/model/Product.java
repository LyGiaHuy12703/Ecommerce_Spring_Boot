package com.minhbui.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Product {
    @Id
    @GeneratedValue
    Long id;

    @ElementCollection
    Set<String> images;

    String title;
    String description;

    Integer soldQuantity;
    Date createdAt;
    Long minPrice;
    Long maxPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @JsonBackReference
    Shop shop;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    Set<ProductDetail> productDetails = new HashSet<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    Set<ProductDetail> productSkus = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    Event event;

    @PrePersist
    public void onCreate() {
        createdAt = new Date(System.currentTimeMillis());
    }

    boolean disabled = false;

}

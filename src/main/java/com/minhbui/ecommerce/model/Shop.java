package com.minhbui.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    User owner;

    @ElementCollection
    Set<String> images = new HashSet<>();


    String name;
    String avatar;
    String description;



    Date createdAt;

    @OneToMany(mappedBy = "shop",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "shop",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonIgnore
    private List<Category> categories;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<Event> events;

    Boolean disabled = false;

    public void addCategory(Category category) {
        categories.add(category);
        category.setShop(this);
    }
    public void removeCategory(Category category) {
        categories.remove(category);
        category.setShop(null);
    }

}

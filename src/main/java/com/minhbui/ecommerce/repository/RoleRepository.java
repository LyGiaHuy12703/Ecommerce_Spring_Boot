package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.enums.RoleEnum;
import com.minhbui.ecommerce.model.Cart;
import com.minhbui.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r where r.name = :roleName")
    Optional<Role> findByRoleName(RoleEnum roleName);
}

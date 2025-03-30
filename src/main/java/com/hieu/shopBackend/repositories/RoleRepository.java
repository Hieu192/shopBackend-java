package com.hieu.shopBackend.repositories;

import com.hieu.shopBackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Boolean existsByName(String name);
//    Role findById(roleId);
}

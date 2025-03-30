package com.hieu.shopBackend.services;

import com.hieu.shopBackend.dtos.requests.role.RoleCreateRequest;
import com.hieu.shopBackend.models.Role;

import java.util.List;

public interface RoleService {
    Role createRole(RoleCreateRequest req);

    List<Role> getAllRoles();
}

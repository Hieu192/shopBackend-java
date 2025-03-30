package com.hieu.shopBackend.services.impl;

import com.hieu.shopBackend.dtos.requests.role.RoleCreateRequest;
import com.hieu.shopBackend.exceptions.AppException;
import com.hieu.shopBackend.exceptions.ErrorCode;
import com.hieu.shopBackend.models.Role;
import com.hieu.shopBackend.repositories.RoleRepository;
import com.hieu.shopBackend.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(RoleCreateRequest req) {
        String roleName = req.getName();
        if (roleRepository.existsByName(roleName)) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = new Role();

        role.setName(roleName);
        System.out.println("role:"+ role);

        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}

package com.hieu.shopBackend.controllers;

import com.hieu.shopBackend.dtos.requests.role.RoleCreateRequest;
import com.hieu.shopBackend.dtos.responses.ApiResponse;
import com.hieu.shopBackend.models.Role;
import com.hieu.shopBackend.services.RoleService;
import com.hieu.shopBackend.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createRole(@RequestBody RoleCreateRequest req) {
        Role newRole = roleService.createRole(req);
        return ResponseEntity.ok().body(
                ApiResponse.builder().success(true)
                        .message(MessageKeys.CREATE_ROLE_SUCCESS)
                        .result(newRole)
                        .build()
        );
    }
    @GetMapping("/")
    public ResponseEntity<List<Role>> getAllRole() {
        List<Role> roles =roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}

package com.hieu.shopBackend.dtos.requests.role;

import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleCreateRequest {
    @NotBlank(message = MessageKeys.ROLE_REQUIRED)
    private String name;
}

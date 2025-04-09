package com.hieu.shopBackend.dtos.requests.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @JsonProperty("phone_number")
    @NotBlank(message = "PHONE_NUMBER_REQUIRED")
    private String phoneNumber;

    @NotBlank(message = "PASSWORD_REQUIRED")
    private String password;
}

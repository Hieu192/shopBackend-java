package com.hieu.shopBackend.dtos.requests.user;


import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    private String fullName;

    @NotBlank(message = "PHONE_NUMBER_REQUIRED")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "PASSWORD_REQUIRED")
    private String password;

    private String retypePassword;

    private Date dateOfBirth;

    private int facebookAccountId;

    private int googleAccountId;

    @NotNull(message = "ROLE_ID_REQUIRED")
    private Long roleId;
}

package com.hieu.shopBackend.dtos.responses.user;

import com.hieu.shopBackend.models.Role;
import com.hieu.shopBackend.models.User;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponse {

    private Long id;

    private String fullName;

    private String phoneNumber;

    private String address;

    private String password;

    private boolean active;

    private Date dateOfBirth;

    private int facebookAccountId;

    private int googleAccountId;

    private Role role;

    public static UserRegisterResponse fromUser(User user) {
        return UserRegisterResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .password(user.getPassword())
                .active(user.isActive())
                .dateOfBirth(user.getDateOfBirth())
//                .facebookAccountId(user.getFacebookAccountId())
//                .googleAccountId(user.getGoogleAccountId())
                .role(user.getRole())
                .build();
    }
}

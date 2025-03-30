package com.hieu.shopBackend.services;

import com.hieu.shopBackend.dtos.requests.user.UserCreateRequest;
import com.hieu.shopBackend.dtos.requests.user.UserUpdateRequest;
import com.hieu.shopBackend.dtos.responses.user.LoginResponse;
import com.hieu.shopBackend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User createUser(UserCreateRequest userCreateRequest);

    String login(String phoneNumber, String password);

    User updateUser(Long userId, UserUpdateRequest userUpdateRequest);

    Page<User> findAllUser(String keyword, Pageable pageable);

    void resetPassword(Long userId, String newPassword);

    User getUserDetailsFromToken(String token);

    LoginResponse refreshToken (String refreshToken);

    void isBlockOrEnable(Long userId, Boolean active);
}

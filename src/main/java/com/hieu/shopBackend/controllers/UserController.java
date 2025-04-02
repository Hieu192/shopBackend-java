package com.hieu.shopBackend.controllers;

import com.hieu.shopBackend.dtos.requests.user.UserCreateRequest;
import com.hieu.shopBackend.dtos.requests.user.UserLoginRequest;
import com.hieu.shopBackend.dtos.requests.user.UserUpdateRequest;
import com.hieu.shopBackend.dtos.responses.ApiResponse;
import com.hieu.shopBackend.dtos.responses.user.LoginResponse;
import com.hieu.shopBackend.dtos.responses.user.UserPageResponse;
import com.hieu.shopBackend.dtos.responses.user.UserResponse;
import com.hieu.shopBackend.models.Token;
import com.hieu.shopBackend.models.User;
import com.hieu.shopBackend.services.TokenService;
import com.hieu.shopBackend.services.UserService;
import com.hieu.shopBackend.services.impl.TokenServiceImpl;
import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        try {
            if (!userCreateRequest.getPassword().equals(userCreateRequest.getRetypePassword())) {
                return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .message(MessageKeys.PASSWORD_NOT_MATCH)
                        .error(MessageKeys.PASSWORD_NOT_MATCH)
                        .build()
                );
            }

            User newUser = userService.createUser(userCreateRequest);
            return ResponseEntity.ok().body(
                    ApiResponse.builder().success(true)
                            .message(MessageKeys.REGISTER_SUCCESS)
                            .result(UserResponse.fromUser(newUser))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message(MessageKeys.ERROR_MESSAGE)
                    .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody UserLoginRequest userLoginRequest,
                                                HttpServletRequest request
                                                ) {
        String tokenGenerator = userService.login(userLoginRequest.getPhoneNumber(), userLoginRequest.getPassword());
        // check is mobile or web login
        String userAgent = request.getHeader("User-Agent");
        User user = userService.getUserDetailsFromToken(tokenGenerator);
        System.out.println("test controller::: ");
        Token token = tokenService.addTokenAndRefreshToken(user, tokenGenerator, isMoblieDevice(userAgent));
        System.out.println("token: " + token);

        ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
                .success(true)
                .message(MessageKeys.LOGIN_SUCCESS)
                .result(LoginResponse.builder()
                        .token(token.getToken())
                        .refreshToken(token.getRefreshToken())
                        .build())
                .build();
        return ResponseEntity.ok().body(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<?>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .success(true)
                .message(MessageKeys.MESSAGE_UPDATE_GET)
                .result(UserResponse.fromUser(user))
                .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/reset-password/{userId}")
    public ResponseEntity<ApiResponse<?>> resetPassword(@PathVariable("userId") Long userId) {
        System.out.println("controller");
        String newPasword = UUID.randomUUID().toString().substring(0, 8);
        userService.resetPassword(userId, newPasword);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message(MessageKeys.RESET_PASSWORD_SUCCESS)
                .result("New Password::" + newPasword)
                .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<UserPageResponse> getUserByKeyword(
            @RequestParam(defaultValue = "", name = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("id").ascending());
        Page<UserResponse> usersPage = userService.findAllUser(keyword, pageRequest)
                .map(UserResponse::fromUser);
        List<UserResponse> userResponses = usersPage.getContent();

        return ResponseEntity.ok(UserPageResponse.builder()
                .users(userResponses)
                .pageNumber(page)
                .totalElements(usersPage.getTotalElements()).pageSize(usersPage.getSize())
                .isLast(usersPage.isLast())
                .totalPages(usersPage.getTotalPages())
                .build()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/block/{userId}/{active}")
    public ResponseEntity<ApiResponse<?>> blockOrEnable(
            @Valid @PathVariable("userId") long id,
            @Valid @PathVariable("active") int active
    ) {
        try {
            userService.isBlockOrEnable(id, active > 0);
            if (active > 0) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message(MessageKeys.USER_ID_UNLOCKED)
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message(MessageKeys.USER_ID_LOCKED)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.builder()
                    .error(e.getMessage()).build());
        }
    }


    //kiểm tra xem thiết bị đang đăng nhập có phải mobile không
    private boolean isMoblieDevice(String userAgent) {
        return userAgent.toLowerCase().contains("mobile");
    }
}

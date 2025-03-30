package com.hieu.shopBackend.controllers;

import com.hieu.shopBackend.dtos.requests.user.UserCreateRequest;
import com.hieu.shopBackend.dtos.requests.user.UserLoginRequest;
import com.hieu.shopBackend.dtos.responses.ApiResponse;
import com.hieu.shopBackend.dtos.responses.user.LoginResponse;
import com.hieu.shopBackend.dtos.responses.user.UserRegisterResponse;
import com.hieu.shopBackend.models.Token;
import com.hieu.shopBackend.models.User;
import com.hieu.shopBackend.services.TokenService;
import com.hieu.shopBackend.services.UserService;
import com.hieu.shopBackend.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                            .result(UserRegisterResponse.fromUser(newUser))
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
        System.out.println("conttroller test::");
        String tokenGenerator = userService.login(userLoginRequest.getPhoneNumber(), userLoginRequest.getPassword());
        System.out.println("conttroller test 1::");
        // check is mobile or web login
        String userAgent = request.getHeader("User-Agent");
        User user = userService.getUserDetailsFromToken(tokenGenerator);
        Token token = tokenService.addTokenAndRefreshToken(user, tokenGenerator, isMoblieDevice(userAgent));
        System.out.println("conttroller test::2" + token);
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

//    @PostMapping("/login")
//    public String login(@RequestBody UserLoginRequest userLoginRequest,
//                                                HttpServletRequest request
//    ) {
//        System.out.println("conttroller test::");
//        String tokenGenerator = userService.login(userLoginRequest.getPhoneNumber(), userLoginRequest.getPassword());
//        System.out.println("conttroller test 1::");
//        // check is mobile or web login
//        String userAgent = request.getHeader("User-Agent");
//        User user = userService.getUserDetailsFromToken(tokenGenerator);
//        Token token = tokenService.addTokenAndRefreshToken(user, tokenGenerator, isMoblieDevice(userAgent));
//        System.out.println("conttroller test::2");
//        ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
//                .success(true)
//                .message(MessageKeys.LOGIN_SUCCESS)
//                .result(LoginResponse.builder()
//                        .token(token.getToken())
//                        .refreshToken(token.getRefreshToken())
//                        .build())
//                .build();
//        return userService.login(userLoginRequest.getPhoneNumber(), userLoginRequest.getPassword());
//    }

    //kiểm tra xem thiết bị đang đăng nhập có phải mobile không
    private boolean isMoblieDevice(String userAgent) {
        return userAgent.toLowerCase().contains("mobile");
    }
}

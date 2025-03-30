package com.hieu.shopBackend.services.impl;

import com.hieu.shopBackend.dtos.requests.user.UserCreateRequest;
import com.hieu.shopBackend.dtos.requests.user.UserUpdateRequest;
import com.hieu.shopBackend.dtos.responses.user.LoginResponse;
import com.hieu.shopBackend.exceptions.AppException;
import com.hieu.shopBackend.exceptions.ErrorCode;
import com.hieu.shopBackend.exceptions.payload.DataNotFoundException;
import com.hieu.shopBackend.mappers.UserMapper;
import com.hieu.shopBackend.models.Role;
import com.hieu.shopBackend.models.User;
import com.hieu.shopBackend.repositories.RoleRepository;
import com.hieu.shopBackend.repositories.UserRepository;
import com.hieu.shopBackend.services.UserService;
import com.hieu.shopBackend.utils.MessageKeys;
import com.hieu.shopBackend.components.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public User createUser(UserCreateRequest userCreateRequest) {
        String phoneNumber = userCreateRequest.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataNotFoundException(MessageKeys.PHONE_NUMBER_EXISTED);
        }
        Role role = roleRepository.findById(userCreateRequest.getRoleId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.ROLE_NOT_FOUND));
        if (role.getName().equalsIgnoreCase("ROLE_ADMIN")) {
            throw new RuntimeException("CAN_NOT_CREATE_ACCOUNT_ROLE_ADMIN");
        }
        User newUser = userMapper.toUser(userCreateRequest);
        newUser.setRole(role);
        newUser.setActive(true);

        // kiểm tra xem nếu có accontId, không yêu cầu passowrd
        if (userCreateRequest.getFacebookAccountId() == 0 && userCreateRequest.getGoogleAccountId() == 0) {
            String password = userCreateRequest.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }

        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        System.out.println("hello test 1" + phoneNumber);
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.PHONE_NUMBER_AND_PASSWORD_FAILED));
        System.out.println("hello test 2");
        if (user.getFacebookAccountId() == 0 && user.getGoogleAccountId() == 0
                && !passwordEncoder.matches(password, user.getPassword())) {
            throw new DataNotFoundException(MessageKeys.PHONE_NUMBER_AND_PASSWORD_FAILED);
        }
        System.out.println("hello test 3");
        System.out.println("hello test");
        if (!user.isActive()) {
            throw new DataNotFoundException(MessageKeys.USER_ID_LOCKED);
        }
        System.out.println("hello test");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getPhoneNumber(),
                password,
                user.getAuthorities())
        );

        return jwtTokenUtil.generateToken(user);
    }

//    @Override
//    public String login(String phoneNumber, String password) {
//        System.out.println("hello test 1" + phoneNumber);
////        Long id = 13L;
////        Optional<User> user = userRepository.findById(id);
//        User user = userRepository.findByPhoneNumber(phoneNumber)
//                .orElseThrow(() -> new DataNotFoundException(MessageKeys.PHONE_NUMBER_AND_PASSWORD_FAILED));
//        System.out.println("hello test 2");
//        return "hello world";
//    }

    @Override
    public User updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        return null;
    }

    @Override
    public Page<User> findAllUser(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public void resetPassword(Long userId, String newPassword, String checkPassword) {

    }

    @Override
    public User getUserDetailsFromToken(String token) {
        return null;
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public void isBlock(Long userId, Boolean active) {

    }
}

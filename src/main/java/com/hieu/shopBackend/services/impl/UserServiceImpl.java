package com.hieu.shopBackend.services.impl;

import com.hieu.shopBackend.dtos.requests.user.UserCreateRequest;
import com.hieu.shopBackend.dtos.requests.user.UserUpdateRequest;
import com.hieu.shopBackend.dtos.responses.user.LoginResponse;
import com.hieu.shopBackend.exceptions.AppException;
import com.hieu.shopBackend.exceptions.ErrorCode;
import com.hieu.shopBackend.exceptions.payload.DataNotFoundException;
import com.hieu.shopBackend.exceptions.payload.PermissionDenyException;
import com.hieu.shopBackend.mappers.UserMapper;
import com.hieu.shopBackend.models.Role;
import com.hieu.shopBackend.models.Token;
import com.hieu.shopBackend.models.User;
import com.hieu.shopBackend.repositories.RoleRepository;
import com.hieu.shopBackend.repositories.TokenRepository;
import com.hieu.shopBackend.repositories.UserRepository;
import com.hieu.shopBackend.services.TokenService;
import com.hieu.shopBackend.services.UserService;
import com.hieu.shopBackend.utils.MessageKeys;
import com.hieu.shopBackend.components.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

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
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.PHONE_NUMBER_AND_PASSWORD_FAILED));
        if (user.getFacebookAccountId() == 0 && user.getGoogleAccountId() == 0
                && !passwordEncoder.matches(password, user.getPassword())) {
            throw new DataNotFoundException(MessageKeys.PHONE_NUMBER_AND_PASSWORD_FAILED);
        }
        if (!user.isActive()) {
            throw new DataNotFoundException(MessageKeys.USER_ID_LOCKED);
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getPhoneNumber(),
                password,
                user.getAuthorities())
        );

        return jwtTokenUtil.generateToken(user);
    }

    @Override
    public User updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        User existsUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));
        String updatePhoneNumber = userUpdateRequest.getPhoneNumber();
        if (!existsUser.getPhoneNumber().equals(updatePhoneNumber)
                && userRepository.existsByPhoneNumber(updatePhoneNumber)) {
            throw new DataIntegrityViolationException(MessageKeys.PASSWORD_NOT_MATCH);
        }

        if (userUpdateRequest.getFullName() != null) {
            existsUser.setFullName(userUpdateRequest.getFullName());
        }
        if (userUpdateRequest.getPhoneNumber() != null) {
            existsUser.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        }
        if (userUpdateRequest.getAddress() != null) {
            existsUser.setAddress(userUpdateRequest.getAddress());
        }
        if (userUpdateRequest.getDateOfBirth() != null) {
            existsUser.setDateOfBirth(userUpdateRequest.getDateOfBirth());
        }
        if (userUpdateRequest.getFacebookAccountId() > 0) {
            existsUser.setFacebookAccountId(userUpdateRequest.getFacebookAccountId());
        }
        if (userUpdateRequest.getGoogleAccountId() > 0) {
            existsUser.setGoogleAccountId(userUpdateRequest.getGoogleAccountId());
        }
        // Thêm điều kiện so sánh mật khẩu cũ với mk trong db, đúng thì đổi mk
        if (userUpdateRequest.getUpdatePassword() != null && !userUpdateRequest.getUpdatePassword().isEmpty()) {
            String newPassword = passwordEncoder.encode(userUpdateRequest.getUpdatePassword());
            existsUser.setPassword(newPassword);
        }
        return userRepository.save(existsUser);
    }

    @Override
    public Page<User> findAllUser(String keyword, Pageable pageable) {
        System.out.println("findAllUser::"+ keyword);
        return userRepository.findAllByKeyword(keyword, pageable);
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));
        String encodePass = passwordEncoder.encode(newPassword);
        user.setPassword(encodePass);
        userRepository.save(user);

        // thay đổi mật khẩu phải xóa hết token đi
        List<Token> tokens = tokenRepository.findByUser(user);
        tokenRepository.deleteAll(tokens);
    }

    @Override
    public User getUserDetailsFromToken(String token) {
        if (jwtTokenUtil.isTokenExpirated(token)) {
            throw new DataNotFoundException(MessageKeys.TOKEN_EXPIRATION_TIME);
        }

        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new DataNotFoundException(MessageKeys.USER_NOT_FOUND);
        }
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        Token token = tokenService.verifyRefreshToken(refreshToken);

        if (token.getExpirationTime().isBefore(Instant.now())) {
            throw new PermissionDenyException(MessageKeys.APP_PERMISSION_DENY_EXCEPTION);
        }
        return LoginResponse.builder()
                .token(jwtTokenUtil.generateToken(token.getUser()))
                .refreshToken(token.getRefreshToken())
                .build();
    }

    @Override
    public void isBlockOrEnable(Long userId, Boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));
        user.setActive(active);
        userRepository.save(user);
    }
}

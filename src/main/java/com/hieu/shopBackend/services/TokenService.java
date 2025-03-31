package com.hieu.shopBackend.services;

import com.hieu.shopBackend.models.Token;
import com.hieu.shopBackend.models.User;

import java.util.List;

public interface TokenService {
    Token addTokenAndRefreshToken(User user, String token, boolean isMobile);

    Token verifyRefreshToken(String refreshToken);

    List<Token> tokens();
}

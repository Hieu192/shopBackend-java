package com.hieu.shopBackend.services;

import com.hieu.shopBackend.models.Token;
import com.hieu.shopBackend.models.User;

public interface TokenService {
    Token addTokenAndRefreshToken(User user, String token, boolean isMobile);
}

package com.hieu.shopBackend.services.impl;

import com.hieu.shopBackend.exceptions.payload.DataNotFoundException;
import com.hieu.shopBackend.models.Token;
import com.hieu.shopBackend.models.User;
import com.hieu.shopBackend.repositories.TokenRepository;
import com.hieu.shopBackend.repositories.UserRepository;
import com.hieu.shopBackend.services.TokenService;
import com.hieu.shopBackend.utils.MessageKeys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl  implements TokenService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    @Override
    public Token addTokenAndRefreshToken(User user, String token, boolean isMobile) {
        List<Token> userTokens = tokenRepository.findByUser(user);
        System.out.println("token 1::" + token);
        System.out.println("token user ::" + userTokens);

        int tokenCount = userTokens.size();
        if (tokenCount >= MAX_TOKENS) {
            boolean hasNoMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNoMobileToken) {
                tokenToDelete = userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(userTokens.get(0));
            } else {
                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }

        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .refreshToken(UUID.randomUUID().toString())
                .tokenType("Bearer")
                .expirationTime(Instant.now().plusMillis(expiration))
                .refreshExpirationTime(Instant.now().plusMillis(expirationRefreshToken))
                .revoked(false)
                .expired(false)
                .isMobile(isMobile)
                .build();

        System.out.println("token 2::" + newToken);
        return tokenRepository.save(newToken);
    }

    @Override
    public List<Token> tokens() {
        return tokenRepository.findAll();
    }

    public Token verifyRefreshToken(String refreshToken) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));

        if (token.getExpirationTime().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }
}

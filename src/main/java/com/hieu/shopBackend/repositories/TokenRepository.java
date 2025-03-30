package com.hieu.shopBackend.repositories;

import com.hieu.shopBackend.models.Token;
import com.hieu.shopBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUser(User user);
    Token findByToken(String token);
    Optional<Token> findByRefreshToken(String refreshToken);
}

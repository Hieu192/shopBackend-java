package com.hieu.shopBackend.controllers;

import com.hieu.shopBackend.models.Token;
import com.hieu.shopBackend.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    @GetMapping("/all")
    public ResponseEntity<List<Token>> tokens() {
        return ResponseEntity.ok(tokenService.tokens());
    }
}

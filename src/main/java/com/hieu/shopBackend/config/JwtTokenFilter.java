package com.hieu.shopBackend.config;


import com.hieu.shopBackend.components.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    /**
     * 
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response); // enable bypass
                return;
            }

            String authHeader = request.getHeader("Authorization");
            String token;
            String phoneNumber;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            token = authHeader.substring(7);
            phoneNumber = jwtTokenUtil.extractPhoneNumber(token);

            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(phoneNumber);
                if (jwtTokenUtil.isTokenValid(token, user)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isByPassToken(@NotNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(

                Pair.of("/api/v1/roles**", "GET"),
                Pair.of("/api/v1/roles**", "GET"),
                Pair.of("/api/v1/products**", "GET"),
                Pair.of("/api/v1/categories**", "GET"),
                Pair.of("/api/v1/comments**", "GET"),
                Pair.of("/api/v1/users/login", "POST"),
                Pair.of("/api/v1/users/register", "POST"),
                Pair.of("/api/v1/users/refresh-token", "POST")


                // sagger-ui
//                Pair.of("/v2/api-docs", "GET"),
//                Pair.of("/v3/api-docs", "GET"),
//                Pair.of("/v3/api-docs/**", "GET"),
//                Pair.of("/swagger-resources/**", "GET"),
//                Pair.of("/swagger-ui.html", "GET"),
//                Pair.of("/webjars/**", "GET"),
//                Pair.of("/swagger-resources/configuration/ui", "GET"),
//                Pair.of("/swagger-resources/configuration/security", "GET"),
//                Pair.of("/swagger-ui.html/**", "GET"),
//                Pair.of("/swagger-ui/**", "GET"),
//                Pair.of("/swagger-ui.html/**", "GET")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        // check ở method get của order
        if (requestPath.equals("/api/v1/orders")
                && requestMethod.equals("GET")) {
            // check if the requestPath matches the desired pattern
            if (requestPath.matches("/api/v1/orders/\\d+")) {
                return true;
            }
            // if the requestPath is just
            if (requestPath.matches("/api/v1/orders")) {
                return true;
            }
        }

        for (Pair<String, String> bypassToken : bypassTokens) {
            String path = bypassToken.getFirst();
            String method = bypassToken.getSecond();

            if (requestPath.matches(path.replace("**", ".*")) &&
                    requestMethod.equalsIgnoreCase(method)) {
                return true;
            }
        }

        return false;
    }
}

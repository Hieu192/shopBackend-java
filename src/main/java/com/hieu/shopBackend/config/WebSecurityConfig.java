package com.hieu.shopBackend.config;


import com.hieu.shopBackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserRepository userRepository;
    private final JwtTokenFilter jwtTokenFilter;
    private final AuthenticationProvider authenticationProvider;

    public static final String[] PUBLIC_ENDPOINT = {
            "/api/v1/users/register",
            "/api/v1/users/login",
            "/api/v1/users/refresh-token"
    };

    public static final String[] PUBLIC_GET_ENDPOINT = {
            "/api/v1/products/**",
            "/api/v1/products/images/*",
            "/api/v1/orders/**",
            "/api/v1/orders_details/**",
            "/api/v1/roles/**",
            "/api/v1/health_check/**",
            "/api/v1/actuator/**",
            "/api/v1/comments/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(req -> req
                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINT)
                .permitAll()
                .requestMatchers(PUBLIC_ENDPOINT).permitAll()
                .anyRequest().authenticated()
        );
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider);


        return http.build();
    };

}

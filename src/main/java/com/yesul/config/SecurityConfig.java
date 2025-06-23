package com.yesul.config;

import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 추후 AdminUserDetailsService Security 작업 완료 후 추가
//    private final AdminUserDetailsService adminUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//                .userDetailsService(adminUserDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/admin/login",
                                "/admin/css/**", "/admin/js/**", "/admin/img/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")      // form action 과 맞춰야 함
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/admin/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain allFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/user/**", "/register/**", "/error", "/user/assets/**",
                                "/css/**", "/js/**", "/img/**", "/fonts/**"
                        ).permitAll()
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
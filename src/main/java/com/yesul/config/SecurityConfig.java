package com.yesul.config;

import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yesul.user.service.CustomOAuth2UserService;
import com.yesul.user.service.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService adminUserDetailsService;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService oAuth2MemberService;

    // @RequiredArgsConstructor 제거, @Qualifier로 직접 명시, Ambiguty 처리
    public SecurityConfig(
            @Qualifier("userDetailsServiceImpl") UserDetailsService adminUserDetailsService,
            CustomUserDetailsService customUserDetailsService,
            CustomOAuth2UserService oAuth2MemberService) {
        this.adminUserDetailsService = adminUserDetailsService;
        this.customUserDetailsService = customUserDetailsService;
        this.oAuth2MemberService = oAuth2MemberService;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
                .userDetailsService(adminUserDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/admin/login", "/asserts/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
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
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

    // 일반 유저 Security 설정
    @Bean
    @Order(2)
    public SecurityFilterChain allFilterChain(HttpSecurity http, DaoAuthenticationProvider daoAuth) throws Exception {
        http
                .authenticationProvider(daoAuth)
                .securityMatcher("/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/user/verify-email",
                                "/user/regist",
                                "/user/regist-process",
                                "/", "/main", "/user/assets/**", "/community/**", "/error",
                                "/assets/**",
                                "/asserts/**",
                                "/images/**",
                                "/login",
                                "/logout",
                                "/login/oauth2/**",
                                "/oauth2/**",
                                "/post-images/upload"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureHandler((request, response, exception) -> {
                            exception.printStackTrace();
                            response.sendRedirect("/login?error");
                        })
                        .defaultSuccessUrl("/user/profile")
                        .permitAll()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .defaultSuccessUrl("/user/profile")
                        .failureHandler((request, response, exception) -> {
                            exception.printStackTrace();
                            response.sendRedirect("/login?error");
                        })
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2MemberService)
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
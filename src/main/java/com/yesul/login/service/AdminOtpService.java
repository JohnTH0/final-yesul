package com.yesul.login.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.yesul.admin.model.dto.auth.LoginAdmin;
import com.yesul.admin.model.entity.Admin;
import com.yesul.exception.handler.AdminNotFoundException;
import com.yesul.login.repository.AdminLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOtpService {

    private final GoogleAuthenticator Auth;
    private final AdminLoginRepository adminLoginRepository;

    public Admin findByUsername(String loginId) {
        Admin fetchedAdmin = adminLoginRepository.findByLoginId(loginId)
                .orElseThrow(() -> new AdminNotFoundException("해당 관리자가 존재하지 않습니다."));
        return fetchedAdmin;
    }

    public boolean verifyOtpAndAuthenticate (int otpCode, Authentication authentication){
        LoginAdmin loginAdmin = (LoginAdmin) authentication.getPrincipal();
        String loginId = loginAdmin.getUsername();

        Admin admin = findByUsername(loginId);
        String secretKey = admin.getOtpSecretKey();

        boolean isCodeValid = Auth.authorize(secretKey, otpCode);

        if (!isCodeValid) { return false; }

        List<GrantedAuthority> newAuthorities = List.of(new SimpleGrantedAuthority("ADMIN"));

        LoginAdmin updatedPrincipal = new LoginAdmin(
                loginAdmin.getId(),
                loginAdmin.getUsername(),
                loginAdmin.getPassword(),
                newAuthorities
        );

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedPrincipal,
                loginAdmin.getPassword(),
                newAuthorities
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return true;
    }
}
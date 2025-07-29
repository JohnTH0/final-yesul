package com.yesul.admin.service;

import com.yesul.admin.model.dto.auth.LoginAdmin;
import com.yesul.admin.model.entity.Admin;
import com.yesul.exception.handler.AdminNotFoundException;
import com.yesul.login.repository.AdminLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminLoginRepository adminLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin fetchedAdmin = adminLoginRepository.findByLoginId(username)
                .orElseThrow(() -> new AdminNotFoundException("해당 관리자가 존재하지 않습니다."));

        List<GrantedAuthority> authorityList =
                List.of(new SimpleGrantedAuthority("ADMIN")); //ADMIN_PENDING_OTP

        LoginAdmin admin = LoginAdmin.builder()
                .id(fetchedAdmin.getId())
                .username(fetchedAdmin.getLoginId())
                .password(fetchedAdmin.getLoginPwd())
                .authorities(authorityList)
                .build();

        return admin;
    }

}

package com.yesul.admin.model.dto.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

// 관리자 인증용 (UserDetails 타입)
public class LoginAdminDto extends User {
    private Long id;

    public LoginAdminDto(Long id, String loginId, String loginPwd, Collection<? extends GrantedAuthority> authorities) {
        super(loginId, loginPwd, authorities);
        this.id = id;
    }

    public Long getId() { return id; }
}

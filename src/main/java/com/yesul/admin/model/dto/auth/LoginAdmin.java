package com.yesul.admin.model.dto.auth;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
// 관리자 시큐리티 인증용 (UserDetails 타입)
public class LoginAdmin extends User {
    private Long id;

    public LoginAdmin(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    @Override
    public String toString() {
        return "LoginAdmin{" +
                "id=" + id +
                ", username=" + getUsername() +
                ", password=" + getPassword() +
                ", authorities=" + getAuthorities() +
                '}';
    }

}

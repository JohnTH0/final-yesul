package com.yesul.user.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import lombok.Getter;
import lombok.Setter; // Setter 어노테이션 추가

import com.yesul.user.model.entity.User;

@Getter
@Setter
public class PrincipalDetails implements OAuth2User, UserDetails {
    private User user;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

    public PrincipalDetails(User user, Collection<? extends GrantedAuthority> auths) {
        this.user = user;
        this.authorities = auths;
        this.attributes = null;
    }

    public PrincipalDetails(User user, Map<String, Object> attrs, Collection<? extends GrantedAuthority> auths) {
        this.user = user;
        this.attributes = attrs;
        this.authorities = auths;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == '1';
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
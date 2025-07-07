package com.yesul.user.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("이메일 또는 비밀번호가 잘못되었습니다."));

        // 계정 활성화 상태 확인 (status가 '1'일 때만 활성화)
        if (user.getStatus() != '1') {
            throw new DisabledException("이메일 인증이 필요하거나 계정이 비활성화되었습니다.");
        }

        // PrincipalDetails 객체로 변환하여 반환
        return new PrincipalDetails(
                user,
                List.of(new SimpleGrantedAuthority("USER")) // 일반 사용자 권한 부여
        );
    }
}
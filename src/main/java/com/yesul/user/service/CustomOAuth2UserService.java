package com.yesul.user.service;

import java.util.List;
import java.util.UUID;

import com.yesul.user.auth.NaverUserInfo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.yesul.user.auth.OauthUserInfo;
import com.yesul.user.auth.KakaoUserInfo;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        OauthUserInfo userInfo;
        String registrationId = request.getClientRegistration().getRegistrationId();
        if ("kakao".equals(registrationId)) {
            userInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else if ("naver".equals(registrationId)) {
            userInfo = new NaverUserInfo(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        String email = userInfo.getEmail();
        User user = userRepository.findByEmail(email)
                .filter(user1 -> user1.getStatus() == '1')
                .orElseGet(() -> {
                    User u = User.builder()
                            .email(email)
                            .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                            .name(userInfo.getName())
                            .nickname(userInfo.getName())
                            .type('2')
                            .status('1')
                            .provider(userInfo.getProvider())
                            .providerId(userInfo.getProviderId())
                            .build();
                    return userRepository.save(u);
                });

        // PrincipalDetails로 반환 및 사용자 권한 설정
        return new PrincipalDetails(user, oAuth2User.getAttributes(),
                List.of(new SimpleGrantedAuthority("USER")));
    }
}
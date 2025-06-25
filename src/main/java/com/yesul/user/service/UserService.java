package com.yesul.user.service;

import com.yesul.user.model.dto.UserRegisterDto;
import com.yesul.user.model.entity.User; // User 엔티티 import 추가

public interface UserService {
    /**
     * 일반 사용자 회원가입 처리 (이메일 인증 대기 상태로 인증 메일 발송)
     * @param userRegisterDto 회원가입 요청 DTO
     * @return User Entity
     * @throws IllegalArgumentException 중복검증
     */
    User registerUser(UserRegisterDto userRegisterDto);

    /**
     * 이메일 중복 확인
     * @param email 확인할 이메일
     * @return 중복되면 true, 아니면 false
     */
    boolean isEmailDuplicated(String email);

    /**
     * 닉네임 중복 확인
     * @param nickname 확인할 닉네임
     * @return 중복되면 true, 아니면 false
     */
    boolean isNicknameDuplicated(String nickname);

    /**
     * 이메일 인증 메일 발송
     * @param user 인증 메일을 받을 사용자 엔티티
     */
    void sendVerificationEmail(User user);

    /**
     * 이메일 인증 링크를 통한 사용자 활성화 처리
     * @param email 인증을 요청한 사용자의 이메일
     * @param token 이메일로 발송된 인증 토큰
     * @return 인증 성공 시 true, 실패 시 false
     */
    boolean verifyEmail(String email, String token);
}
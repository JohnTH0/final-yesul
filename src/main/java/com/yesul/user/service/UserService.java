package com.yesul.user.service;

import java.util.Optional;

import com.yesul.user.model.dto.request.UserRegisterRequestDto;
import com.yesul.user.model.dto.request.UserUpdateRequestDto;
import com.yesul.user.model.entity.User;


public interface UserService {
    /**
     * 일반 사용자 회원가입 처리 (이메일 인증 대기 상태로 인증 메일 발송)
     * @param userRegisterRequestDto 회원가입 요청 DTO
     * @return User Entity
     * @throws IllegalArgumentException 중복검증
     */
    User registerUser(UserRegisterRequestDto userRegisterRequestDto);

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
     * 이메일 인증 링크를 통한 사용자 활성화 처리
     * @param email 인증을 요청한 사용자의 이메일
     * @param token 이메일로 발송된 인증 토큰
     * @return 인증 성공 시 true, 실패 시 false
     */
    boolean verifyEmail(String email, String token);

    /**
     * 이메일로 사용자 조회
     * @param email 사용자 이메일
     * @return User 엔티티 (Optional)
     */
    Optional<User> findUserByEmail(String email);

    User updateUserProfile(Long userId, UserUpdateRequestDto dto);

    void changePassword(Long userId, String newPassword);

    /**
     * 회원 탈퇴 처리
     * @param userId 현재 로그인한 사용자 ID
     * @param rawPassword 입력된 현재 비밀번호
     * @throws IllegalArgumentException 비밀번호 불일치 시
     */
    void resignUser(Long userId, String rawPassword);

    /**
     * @throws IllegalArgumentException 유효하지 않은 토큰이거나 만료된 경우
     */
    void resetPassword(String email, String token, String newPassword);

}
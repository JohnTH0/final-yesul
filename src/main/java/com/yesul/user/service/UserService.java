package com.yesul.user.service;

import com.yesul.user.model.dto.UserRegisterDto;

public interface UserService {
    /**
     * 일반 사용자 회원가입 처리
     * @param userRegisterDto 회원가입 요청 DTO
     * @return 성공 여부 (true/false)
     */
    boolean registerUser(UserRegisterDto userRegisterDto);

    /**
     * 이메일 중복 확인
     * @param mail 확인할 이메일
     * @return 중복되면 true, 아니면 false
     */
    boolean isMailDuplicated(String mail);
}
package com.yesul.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.yesul.user.model.dto.UserRegisterDto;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public boolean registerUser(UserRegisterDto userRegisterDto) {
        if (!userRegisterDto.getLoginPwd().equals(userRegisterDto.getConfirmPwd())) {
            log.warn("회원가입 실패: 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return false;
        }

        if (isMailDuplicated(userRegisterDto.getMail())) {
            log.warn("회원가입 실패: 이미 사용 중인 이메일입니다. Email: {}", userRegisterDto.getMail());
            return false;
        }

        try {
            String encodedPassword = passwordEncoder.encode(userRegisterDto.getLoginPwd());
            User newUser = userRegisterDto.toEntity(encodedPassword);
            userRepository.save(newUser);
            return true;
        } catch (Exception e) {
            log.error("회원가입 중 데이터베이스 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isMailDuplicated(String mail) {
        return userRepository.findByMail(mail).isPresent();
    }
}
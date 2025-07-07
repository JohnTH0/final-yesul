package com.yesul.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.yesul.user.model.dto.UserRegisterDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationAsyncService {
    private final UserService userService;

    /**
     * 회원가입 처리 전체를 비동기로 수행
     */
    @Async("asyncExecutor")
    public void registerInBackground(UserRegisterDto dto) {
        try {
            userService.registerUser(dto);
            log.info("Background registration finished for {}", dto.getEmail());
        } catch (Exception e) {
            log.error("Background registration failed for {}: {}", dto.getEmail(), e.getMessage(), e);
        }
    }
}

package com.yesul.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "유저 회원탈퇴 요청")
public class UserResignRequestDto {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;
}

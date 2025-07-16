package com.yesul.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "비밀번호를 분실한 유저의 새 비밀번호 생성")
public class UserPasswordResetRequestDto {

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Size(min = 8, max = 30, message = "비밀번호는 8~30자 사이여야 합니다.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Size(min = 8, max = 30, message = "비밀번호는 8~30자 사이여야 합니다.")
    private String reNewPassword;
}

package com.yesul.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordResetDto {

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Size(min = 8, max = 30, message = "비밀번호는 8~30자 사이여야 합니다.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Size(min = 8, max = 30, message = "비밀번호는 8~30자 사이여야 합니다.")
    private String reNewPassword;
}

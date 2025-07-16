package com.yesul.user.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import com.yesul.user.model.entity.User;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "일반 사용자의 회원가입 요청 ")
public class UserRegisterRequestDto {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @Size(max = 50, message = "이메일은 50자를 초과할 수 없습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 30, message = "비밀번호는 8~30자로 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String confirmPwd;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(max = 20, message = "이름은 20자를 초과할 수 없습니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(max = 20, message = "닉네임은 20자를 초과할 수 없습니다.")
    private String nickname;

    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$", message = "생년월일 8자리를 YYYYMMDD 형식으로 입력해주세요.")
    private String birthday;

    @Size(max = 100, message = "주소는 100자를 초과할 수 없습니다.")
    private String address;

    // DTO To Entity
    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .name(this.name)
                .nickname(this.nickname)
                .birthday(this.birthday)
                .address(this.address)
                .type('1')
                .status('2')
                .profile(null)
                .point(0)
                .build();
    }
}
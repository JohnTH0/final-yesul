package com.yesul.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdateDto {

    @Size(max = 20, message = "이름은 20자를 초과할 수 없습니다.")
    private String name;

    @Size(max = 20, message = "닉네임은 20자를 초과할 수 없습니다.")
    private String nickname;

    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$", message = "생년월일 8자리를YYYYMMDD 형식으로 입력해주세요.")
    private String birthday;

    @Size(max = 100, message = "주소는 100자를 초과할 수 없습니다.")
    private String address;

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @Size(max = 50, message = "이메일은 50자를 초과할 수 없습니다.")
    private String email;

    @Size(min = 8, max = 30, message = "새 비밀번호는 8~30자로 입력해주세요.")
    private String newPassword;
    private String confirmPassword;

    private MultipartFile profileImage;

    private String profile;
}
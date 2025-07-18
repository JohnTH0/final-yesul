package com.yesul.user.model.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "유저 일반정보 변경")
public class UserUpdateRequestDto {

    @Size(max = 20, message = "이름은 20자를 초과할 수 없습니다.")
    private String name;

    @Size(max = 20, message = "닉네임은 20자를 초과할 수 없습니다.")
    private String nickname;

    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$", message = "생년월일 8자리를 YYYYMMDD 형식으로 입력해주세요.")
    private String birthday;

    @Size(max = 100, message = "주소는 100자를 초과할 수 없습니다.")
    private String address;

    @Size(max = 500, message = "자기소개는 500자를 초과할 수 없습니다.")
    @Schema(description = "자기소개")
    private String description;

    private MultipartFile profileImage;
}
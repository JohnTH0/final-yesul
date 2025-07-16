package com.yesul.user.model.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "유저 목록 조회")
public class UserListResponseDto {
    private Long id;
    private String nickname;
    private String email;
    private String birthday;
    private LocalDateTime createdAt;
    private Character status;

    private String displayStatus;

    public UserListResponseDto(Long id, String email, String nickname, String birthday, Character status, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.birthday = birthday;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getFormattedBirthday() {
        return birthday.substring(0, 4) + "-" +
                birthday.substring(4, 6) + "-" +
                birthday.substring(6, 8);
    }
}

package com.yesul.user.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserListDto {
    private Long id;
    private String nickname;
    private String email;
    private String birthday;
    private LocalDateTime createdAt;
    private Character status;

    private String displayStatus;

    public UserListDto(Long id, String email, String nickname, String birthday, Character status, LocalDateTime createdAt) {
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

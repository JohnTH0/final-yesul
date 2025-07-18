package com.yesul.user.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import com.yesul.user.model.entity.User;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "유저 정보 조회")
public class UserProfileResponseDto {

    private String email;
    private String name;
    private String nickname;
    private String birthday;
    private String address;
    private String profile;
    private char type;
    private String provider;
    private String description;
    private Integer point;

    // Entity To Dto
    public UserProfileResponseDto(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.birthday = user.getBirthday();
        this.address = user.getAddress();
        this.profile = user.getProfile();
        this.type = user.getType();
        this.provider = user.getProvider();
        this.description = user.getDescription();
        this.point = user.getPoint();
    }
}
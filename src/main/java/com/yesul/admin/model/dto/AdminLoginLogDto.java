package com.yesul.admin.model.dto;

import com.yesul.admin.model.entity.AdminLoginLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AdminLoginLogDto {
    private Long id;
    private String ip;
    private String userAgent;
    private LocalDateTime loginTime;


    public static AdminLoginLogDto from(AdminLoginLog adminLoginLog) {

        return AdminLoginLogDto.builder()
                .id(adminLoginLog.getId())
                .ip(adminLoginLog.getIp())
                .userAgent(adminLoginLog.getUserAgent())
                .loginTime(adminLoginLog.getCreatedAt())
                .build();
    }

}


package com.yesul.admin.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminDto {
    private Long id;
    private String loginId;
    private String loginPwd;
}

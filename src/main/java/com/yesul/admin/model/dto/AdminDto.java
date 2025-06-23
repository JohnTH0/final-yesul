package com.yesul.admin.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AdminDto {  // DB 조회된 admin 담는 용도
    private Long id;
    private String loginId;
    private String loginPwd;
}

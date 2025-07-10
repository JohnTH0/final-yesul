package com.yesul.login.model.entity;

import com.yesul.alcohol.model.dto.ClovaRequestDto;
import com.yesul.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_login_log")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminLoginLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "user_agent", nullable = false, columnDefinition = "TEXT")
    private String userAgent;

}
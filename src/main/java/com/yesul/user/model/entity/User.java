package com.yesul.user.model.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.*;

import com.yesul.common.BaseTimeEntity;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", length = 68)
    private String password;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "nickname", length = 20)
    private String nickname;

    @Column(name = "birthday", length = 8)
    private String birthday;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "type", nullable = false) // '1': 일반, '2': 소셜
    private Character type;

    @Column(nullable = false, length = 1) // '1': 인증, '2': 미인증, '3': 탈퇴
    private Character status;

    @Column(name = "profile", columnDefinition = "TEXT") // img_url
    private String profile;

    @Column(name = "email_check_token")
    private String emailCheckToken;

    @Column(name = "email_check_token_generated_at")
    private LocalDateTime emailCheckTokenGeneratedAt;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    // 이메일 인증 토큰 설정
    public void generateEmailCheckToken() {
        this.emailCheckToken = java.util.UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    // 이메일 인증 완료 처리
    public void completeSignUp() {
        this.status = '1'; // 활성 상태로 변경
        this.emailCheckToken = null; // 토큰 삭제
        this.emailCheckTokenGeneratedAt = null; // 토큰 생성 시간 삭제
    }

    // 사용자 활성화 상태 변경
    public void setActivatedStatus(char status) {
        this.status = status;
    }

    @Builder
    public User(Long id, String email, String password, String name, String nickname, String birthday, String address, Character type, Character status, String profile) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.birthday = birthday;
        this.address = address;
        this.type = type;
        this.status = status;
        this.profile = profile;
    }
}
package com.yesul.user.model.entity;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "mail", length = 50, unique = true)
    private String mail;

    @Column(name = "login_pwd", length = 68)
    private String loginPwd;

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

    @Column(name = "is_activated", nullable = false)
    private Boolean isActivated; // 1: 회원, 0: 탈퇴

    @Column(name = "profile", columnDefinition = "TEXT") // img_url
    private String profile;

    @Builder
    public User(Long id, String mail, String loginPwd, String name, String nickname, String birthday, String address, Character type, Boolean isActivated, String profile) {
        this.id = id;
        this.loginPwd = loginPwd;
        this.name = name;
        this.mail = mail;
        this.nickname = nickname;
        this.birthday = birthday;
        this.address = address;
        this.type = type;
        this.isActivated = isActivated;
        this.profile = profile;
    }
}
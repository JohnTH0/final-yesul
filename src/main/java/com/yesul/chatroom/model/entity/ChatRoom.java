package com.yesul.chatroom.model.entity;

import com.yesul.admin.model.entity.Admin;
import com.yesul.common.BaseTimeEntity;
import com.yesul.user.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_message", nullable = false)
    private String lastMessage;

    @Column(name = "admin_unread_count", nullable = false)
    private int adminUnreadCount;

    @Column(name = "user_unread_count", nullable = false)
    private int userUnreadCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    public void increaseAdminUnreadCount() {
        this.adminUnreadCount++;
    }

    public void increaseUserUnreadCount() {
        this.userUnreadCount++;
    }

    public void resetAdminUnreadCount() {
        this.adminUnreadCount = 0;
    }

    public void resetUserUnreadCount() {
        this.userUnreadCount = 0;
    }
}


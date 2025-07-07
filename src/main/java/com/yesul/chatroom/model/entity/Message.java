package com.yesul.chatroom.model.entity;

import com.yesul.admin.model.entity.Admin;
import com.yesul.chatroom.model.entity.enums.MessageType;
import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private Type senderType;

    @Column(name = "message_context",length = 5000, nullable = false)
    private String messageContext;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, columnDefinition = "TEXT")
    private MessageType messageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id", nullable = false)
    private ChatRoom chatRoom;

}

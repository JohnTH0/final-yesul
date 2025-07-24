package com.yesul.event.model.entity;

import com.yesul.common.BaseTimeEntity;
import com.yesul.event.model.enums.EventStatus;
import com.yesul.notice.model.entity.Notice;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event_list")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EventList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", length = 100, nullable = false)
    private String userName;

    @Column(name = "user_email", length = 255, nullable = false)
    private String userEmail;

    @Column(name = "form_id", length = 150)
    private String formId;

    @Column(name = "phone", length = 255, nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EventStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Notice notice;
}
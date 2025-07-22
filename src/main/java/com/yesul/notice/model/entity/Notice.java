package com.yesul.notice.model.entity;

import com.yesul.common.BaseTimeEntity;
import com.yesul.notice.model.enums.NoticeType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "content",columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "image_url", length = 200)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private NoticeType type; //GENERAL EVENT

    @Column(name = "form_url", length = 300)
    private String formUrl;

    @Column(name = "form_id", length = 300)
    private String formId;

    @Column(name = "point", columnDefinition = "INT DEFAULT 0")
    private Integer point;

}
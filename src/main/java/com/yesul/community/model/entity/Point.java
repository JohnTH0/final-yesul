package com.yesul.community.model.entity;

import com.yesul.common.BaseTimeEntity;
import com.yesul.community.model.entity.enums.PointType;
import jakarta.persistence.*;
import lombok.*;

/**
 * 활동별 포인트 값 관리 테이블
 */
@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Point extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PointType type;

    @Column(nullable = false)
    private Integer point;   // 해당 활동으로 얻는 포인트 값
}
package com.yesul.community.model.entity;

import com.yesul.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 유저별 포인트 적립/차감 내역 기록 테이블
 */
@Entity
@Table(name = "point_history")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;  // 포인트 기록 주인

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "points_id", nullable = false, foreignKey = @ForeignKey(name = "fk_points"))
    private Point point;  // 어떤 활동의 포인트인지 (points 테이블 FK)

    @Column(nullable = false)
    private Boolean isEarned;  // true: 적립, false: 차감
}
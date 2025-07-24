package com.yesul.admin.chart.model.entity;

import com.yesul.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_stats")
public class DailyStats{

    @Id
    @Column(name = "stat_date")
    private LocalDate statDate;

    @Column(name = "post_count_free")
    private Integer freePostCount;

    @Column(name = "post_count_recipe")
    private Integer recipePostCount;

    @Column(name = "post_count_info")
    private Integer infoPostCount;

    @Column(name = "visitor_count")
    private Integer visitorCount;

}
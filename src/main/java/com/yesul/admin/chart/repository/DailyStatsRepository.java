package com.yesul.admin.chart.repository;

import com.yesul.admin.chart.model.entity.DailyStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyStatsRepository extends JpaRepository<DailyStats, LocalDate> {

    List<DailyStats> findTop7ByOrderByStatDateAsc();
}
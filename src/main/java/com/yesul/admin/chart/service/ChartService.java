package com.yesul.admin.chart.service;

import com.yesul.admin.chart.model.dto.DashboardChartDto;
import com.yesul.admin.chart.model.entity.DailyStats;
import com.yesul.admin.chart.repository.DailyStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final DailyStatsRepository dailyStatsRepository;

    public DashboardChartDto getChartData() {
        List<DailyStats> weeklysStats = dailyStatsRepository.findTop7ByOrderByStatDateAsc();

        List<String> categories = weeklysStats.stream()
                .map(dailyStats -> dailyStats.getStatDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH))
                .collect(Collectors.toList());

        List<Integer> free = weeklysStats.stream().map(DailyStats::getFreePostCount).collect(Collectors.toList());
        List<Integer> recipe = weeklysStats.stream().map(DailyStats::getRecipePostCount).collect(Collectors.toList());
        List<Integer> info = weeklysStats.stream().map(DailyStats::getInfoPostCount).collect(Collectors.toList());
        List<Integer> visitors = weeklysStats.stream().map(DailyStats::getVisitorCount).collect(Collectors.toList());

        DashboardChartDto chartDto = new DashboardChartDto();
        chartDto.setCategories(categories);
        chartDto.setFreeBoard(free);
        chartDto.setRecipeBoard(recipe);
        chartDto.setInfoBoard(info);
        chartDto.setVisitors(visitors);

        return chartDto;
    }
}

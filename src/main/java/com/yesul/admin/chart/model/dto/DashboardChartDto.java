package com.yesul.admin.chart.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DashboardChartDto {
    private List<String> categories;
    private List<Integer> freeBoard;
    private List<Integer> recipeBoard;
    private List<Integer> infoBoard;
    private List<Integer> visitors;
}
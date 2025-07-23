package com.yesul.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventFormRequestDto {
    private String form_id;
    private String form_title;
    private List<QuestionRequestDto> results;
}

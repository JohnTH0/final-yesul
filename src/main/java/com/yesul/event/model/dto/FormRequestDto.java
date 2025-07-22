package com.yesul.event.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 구글 폼 생성시 사용자에게 받을 폼 내용 전달용
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FormRequestDto {
    private String title;
    private String documentTitle;
    private String description;
}

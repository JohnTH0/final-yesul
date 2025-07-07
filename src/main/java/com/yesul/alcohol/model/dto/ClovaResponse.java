package com.yesul.alcohol.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClovaResponse {

    private Status status;
    private Result result;

    @Getter
    @Setter
    public static class Status {
        private String code;
        private String message;
    }

    @Getter
    @Setter
    public static class Result {
        @JsonProperty("finalAnswer")
        private String finalAnswer;
    }
}
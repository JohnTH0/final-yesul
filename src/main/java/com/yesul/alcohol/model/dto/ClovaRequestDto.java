package com.yesul.alcohol.model.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClovaRequestDto {

    private String query;
    private boolean tokenStream = true;

    private List<ChatMessage> chatHistory;

    private RequestOverride requestOverride = RequestOverride.defaultOverride();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessage {
        private String role;
        private String content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestOverride {
        private BaseOperation baseOperation;

        public static RequestOverride defaultOverride() {
            return new RequestOverride(new BaseOperation(new AppQuery("appid-11223344")));
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseOperation {
        private AppQuery query;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppQuery {
        private String appid;
    }
}
package com.yesul.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yesul.event.model.dto.FormRequestDto;
import com.yesul.notice.model.dto.NoticeDto;
import com.yesul.notice.model.enums.NoticeType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public NoticeDto createAndUpdateForm(FormRequestDto request, String accessToken) throws JsonProcessingException {
        String apiUrl = "https://forms.googleapis.com/v1/forms";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("title", request.getTitle());

        Map<String, Object> createFormBody = new HashMap<>();
        createFormBody.put("info", infoMap);

        HttpEntity<Map<String, Object>> createRequest = new HttpEntity<>(createFormBody, headers);
        ResponseEntity<String> createResponse = restTemplate.postForEntity(apiUrl, createRequest, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode formJson = objectMapper.readTree(createResponse.getBody());
        String formId = formJson.get("formId").asText();

        String batchUpdateUrl = "https://forms.googleapis.com/v1/forms/" + formId + ":batchUpdate";

        Map<String, Object> updateInfo = Map.of(
                "info", Map.of("description", request.getDescription()),
                "updateMask", "description"
        );

        Map<String, Object> batchUpdateBody = Map.of(
                "requests", List.of(Map.of("updateFormInfo", updateInfo))
        );

        HttpEntity<Map<String, Object>> updateRequest = new HttpEntity<>(batchUpdateBody, headers);
        restTemplate.postForEntity(batchUpdateUrl, updateRequest, String.class);

        NoticeDto noticeDto = NoticeDto.builder()
                .formId(formId)
                .type(NoticeType.EVENT)
                .formUrl(batchUpdateUrl)
                .build();

        return noticeDto;
    }

}
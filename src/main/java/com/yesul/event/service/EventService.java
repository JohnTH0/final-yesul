package com.yesul.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesul.event.model.dto.EventListDto;
import com.yesul.event.model.entity.EventList;
import com.yesul.event.repository.EventListRepository;
import com.yesul.exception.handler.EntityNotFoundException;
import com.yesul.notice.model.dto.NoticeDto;
import com.yesul.notice.model.entity.Notice;
import com.yesul.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventService {

    private final RestTemplate restTemplate;
    private final NoticeRepository noticeRepository;
    private final EventListRepository eventListRepository;
    private final ModelMapper modelMapper;

    public String createAndGetFormId(String accessToken) throws JsonProcessingException {
        String apiUrl = "https://forms.googleapis.com/v1/forms";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("title", "이벤트 제목을 작성하세요.");

        Map<String, Object> createFormBody = new HashMap<>();
        createFormBody.put("info", infoMap);

        HttpEntity<Map<String, Object>> createRequest = new HttpEntity<>(createFormBody, headers);
        ResponseEntity<String> createResponse = restTemplate.postForEntity(apiUrl, createRequest, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode formJson = objectMapper.readTree(createResponse.getBody());
        String formId = formJson.get("formId").asText();

        return formId;
    }

    public void addQuestions(String formId, String accessToken) throws IOException {
        String url = "https://forms.googleapis.com/v1/forms/" + formId + ":batchUpdate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestBody = Map.of(
                "requests", List.of(
                        Map.of("createItem", Map.of(
                                "item", Map.of(
                                        "title", "성함을 적어주세요",
                                        "questionItem", Map.of(
                                                "question", Map.of(
                                                        "required", true,
                                                        "textQuestion", Map.of()
                                                )
                                        )
                                ),
                                "location", Map.of("index", 0)
                        )),
                        Map.of("createItem", Map.of(
                                "item", Map.of(
                                        "title", "아이디를 정확하게 적어주세요",
                                        "questionItem", Map.of(
                                                "question", Map.of(
                                                        "required", true,
                                                        "textQuestion", Map.of()
                                                )
                                        )
                                ),
                                "location", Map.of("index", 1)
                        )),
                        Map.of("createItem", Map.of(
                                "item", Map.of(
                                        "title", "연락처",
                                        "questionItem", Map.of(
                                                "question", Map.of(
                                                        "required", true,
                                                        "textQuestion", Map.of()
                                                )
                                        )
                                ),
                                "location", Map.of("index", 2)
                        ))
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForEntity(url, entity, String.class);
    }

    public NoticeDto getEventIdByFormId(String formId) {
        Notice event = noticeRepository.findByFormId(formId);
        if (event == null) {
            throw new EntityNotFoundException("해당 form 아이디의 이벤트를 찾을 수 없습니다: " + formId);
        }
        return modelMapper.map(event, NoticeDto.class);
    }

    public void saveEventList(EventListDto event) {
        Notice notice = noticeRepository.findById(event.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("이벤트를 찾을 수 없습니다."));

        EventList eventList = EventList.builder()
                .formId(event.getFormId())
                .userName(event.getUserName())
                .userEmail(event.getUserEmail())
                .phone(event.getPhone())
                .status(event.getStatus())
                .notice(notice)
                .build();

        eventListRepository.save(eventList);
    }
}
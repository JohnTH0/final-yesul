package com.yesul.alcohol.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesul.alcohol.model.dto.Clova2RequestDto;
import com.yesul.alcohol.model.dto.ClovaRequestDto;
import com.yesul.alcohol.model.dto.ClovaAskRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClovaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String callClovaAPI(ClovaAskRequestDto askDto) {
        String url = "https://clovastudio.stream.ntruss.com/testapp/v1/skillsets/fv46jc9v/versions/58/final-answer";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer nv-ae4562f3d5a54905a8ae4563a0ad9ce7eYf5");
        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", "7ecdb527711449cb8dc6827b6212b3d5");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ClovaRequestDto dto = ClovaRequestDto.builder()
                .query(askDto.getAsk())
                .tokenStream(true)
                .requestOverride(ClovaRequestDto.RequestOverride.defaultOverride())
                .build();

        HttpEntity<ClovaRequestDto> entity = new HttpEntity<>(dto, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class
            );
            return response.getBody();
        } catch (Exception e) {
            return "Clova 호출 실패: " + e.getMessage();
        }
    }

    public String callClovaAPI2(ClovaAskRequestDto askDto) {
        String url = "https://clovastudio.stream.ntruss.com/testapp/v3/chat-completions/HCX-005";

        // 메시지 구성
        List<Clova2RequestDto.Message> messages = List.of(
                new Clova2RequestDto.Message("system",
                        "- 응답에 대해서 술에 대한 정보는, 이름과 지역만 발췌하여 해당 주류와 어울리는 여행일정만 제공합니다.\n" +
                                "- 지역에 대한 정보가 없으면 최대한 응답과 관련된 여행지를 추천해줍니다.\n" +
                                "- 추천 여행지에 대한 정보를 가보고 싶게 소개합니다. \n" +
                                "- 여행일정은 1박2일 또는 2박3일로 제공합니다.\n" +
                                "- 여행 일정을 가독성 좋게 제공합니다.\n" +
                                "- 주제별로 띄워쓰기를 적극 활용합니다."),
                new Clova2RequestDto.Message("user", askDto.getAsk())
        );

        // DTO 구성
        Clova2RequestDto requestDto = Clova2RequestDto.builder()
                .messages(messages)
                .topP(0.6)
                .topK(0)
                .maxTokens(512)
                .temperature(0.3)
                .repetitionPenalty(1.1)
                .stop(List.of())
                .includeAiFilters(true)
                .seed(0)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer nv-ae4562f3d5a54905a8ae4563a0ad9ce7eYf5");
        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", "6a453e2468bf4f3db6095e617409809d");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Clova2RequestDto> entity = new HttpEntity<>(requestDto, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class
            );
            return response.getBody();
        } catch (Exception e) {
            return "Clova 호출 실패: " + e.getMessage();
        }
    }
}

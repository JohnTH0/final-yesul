package com.yesul.alcohol.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        String url = "https://clovastudio.stream.ntruss.com/testapp/v1/skillsets/fv46jc9v/versions/18/final-answer";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer nv-ae4562f3d5a54905a8ae4563a0ad9ce7eYf5");
        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", "d45d3610c48e459bb10f4bcba5488733");
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
}

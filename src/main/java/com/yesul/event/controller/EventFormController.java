package com.yesul.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesul.event.model.dto.EventFormRequestDto;
import com.yesul.event.model.dto.EventListDto;
import com.yesul.event.model.dto.QuestionRequestDto;
import com.yesul.event.model.enums.EventStatus;
import com.yesul.event.service.EventService;
import com.yesul.exception.handler.UnauthorizedAccessException;
import com.yesul.notice.model.dto.NoticeDto;
import com.yesul.notice.service.NoticeService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/event")
public class EventFormController {

    private final EventService eventService;
    private final NoticeService noticeService;
    private final RestTemplate restTemplate;

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    private static final String scope = "https://www.googleapis.com/auth/forms.body https://www.googleapis.com/auth/drive.file";

    @GetMapping("/oauth2/authorize")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String authUrl = UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", scope)
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .build().toUriString();

        response.sendRedirect(authUrl);
    }

    @GetMapping("/oauth2/callback")
    public String googleCallback(@RequestParam("code") String code, HttpSession session) throws JsonProcessingException {
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpoint, request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());

        String accessToken = responseJson.get("access_token").asText();

        session.setAttribute("googleAccessToken", accessToken);


        return "redirect:/admin/event/create/form";
    }

    @GetMapping("/create")
    public String createPage() {
        return "admin/event/create-form";
    }

    @GetMapping("/create/form")
    public String createForm(HttpSession session, Model model) throws IOException {
        String accessToken = (String) session.getAttribute("googleAccessToken");

        if (accessToken == null) {
            throw new UnauthorizedAccessException("Google 인증이 필요합니다.");
        }

        String formId = eventService.createAndGetFormId(accessToken);
        eventService.addQuestions(formId, accessToken);

        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setFormId(formId);

        model.addAttribute("noticeDto", noticeDto);
        return "admin/notice/regist";
    }

    @GetMapping
    public String eventPage(Pageable pageable, Model model) {
        Page<NoticeDto> noticeEventListPageable = noticeService.findNoticeEventList(pageable);
        model.addAttribute("noticeListPageable", noticeEventListPageable);
        return "admin/event/list";
    }

    @PostMapping("/list")
    public ResponseEntity<String> receiveFormResponses(@RequestBody EventFormRequestDto request) {
        String formId = request.getForm_id();

        NoticeDto event = eventService.getEventIdByFormId(formId);

        String userName = null;
        String userEmail = null;
        String phone = null;

        for (QuestionRequestDto question : request.getResults()) {
            String title = question.getTitle();
            String response = question.getResponse().toString();
            if (title.contains("성함")) {
                userName = response;
            } else if (title.contains("아이디")) {
                userEmail = response;
            } else if (title.contains("연락처")) {
                phone = response;
            }
        }

        EventListDto eventApply = EventListDto.builder()
                .formId(formId)
                .userName(userName)
                .userEmail(userEmail)
                .phone(phone)
                .status(EventStatus.NEW)
                .eventId(event.getId())
                .build();

        eventService.saveEventList(eventApply);

        return ResponseEntity.ok("데이터 수신 성공");
    }

}
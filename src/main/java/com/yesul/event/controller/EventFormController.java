package com.yesul.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesul.event.model.dto.EventFormRequestDto;
import com.yesul.event.model.dto.FormRequestDto;
import com.yesul.event.model.dto.QuestionRequestDto;
import com.yesul.event.service.EventService;
import com.yesul.notice.model.dto.NoticeDto;
import com.yesul.notice.model.enums.NoticeType;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/event")
public class EventFormController {

    private final EventService eventService;
    private final NoticeService noticeService;
    private final RestTemplate restTemplate;

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
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


        return "redirect:/admin/event/create";
    }

    @GetMapping("/create")
    public String createPage() {
        return "/admin/event/create-form";
    }

    @PostMapping("/create/form")
    public String createForm(@ModelAttribute FormRequestDto request, HttpSession session, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        String accessToken = (String) session.getAttribute("googleAccessToken");

        if (accessToken == null) {
            redirectAttributes.addFlashAttribute("error", "Google 인증이 필요합니다.");
            return "redirect:/admin/event/oauth2/authorize";
        }

        NoticeDto notice = eventService.createAndUpdateForm(request, accessToken);
        redirectAttributes.addFlashAttribute("noticeDto", notice);

        return "redirect:/admin/notice/regist";
    }

    @GetMapping
    public String eventPage(Pageable pageable, Model model) {
        Page<NoticeDto> noticeEventListPageable = noticeService.findNoticeEventList(pageable);
        model.addAttribute("noticeListPageable", noticeEventListPageable);
        return "/admin/event/list";
    }

    @PostMapping("/list")
    public ResponseEntity<String> receiveFormResponses(@RequestBody EventFormRequestDto request) {
        System.out.println("폼 ID: " + request.getForm_id());
        System.out.println("폼 제목: " + request.getForm_title());

        for (QuestionRequestDto question : request.getResults()) {
            System.out.println(question.getType());
            System.out.println("질문: " + question.getTitle());
            System.out.println("응답: " + question.getResponse());
        }

        return ResponseEntity.ok("데이터 수신 성공");
    }

}
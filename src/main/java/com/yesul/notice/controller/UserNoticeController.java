package com.yesul.notice.controller;

import com.yesul.notice.model.dto.NoticeDto;
import com.yesul.notice.service.NoticeService;
import com.yesul.util.ImageUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notices")
@Controller
public class UserNoticeController {

    private final NoticeService noticeService;

    @GetMapping("")
    public String notice(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<NoticeDto> noticeListPageable = noticeService.findNoticeList(pageable);
        model.addAttribute("noticeListPageable", noticeListPageable);
        return "notice/list";
    }

    @GetMapping("/{id}")
    public String noticeDetail(@PathVariable Long id, Model model) {
        NoticeDto notice = noticeService.findById(id);
        model.addAttribute("notice", notice);
        return "notice/detail";
    }
}
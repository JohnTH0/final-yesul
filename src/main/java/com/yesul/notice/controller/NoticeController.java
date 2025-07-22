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
@RequestMapping("/admin/notice")
@Controller
public class NoticeController {

    private final NoticeService noticeService;
    private final ImageUpload imageUpload;

    @GetMapping
    public String notice(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<NoticeDto> noticeListPageable = noticeService.findNoticeList(pageable);
        model.addAttribute("noticeListPageable", noticeListPageable);
        return "admin/notice/list";
    }

    @GetMapping("/{id}")
    public String noticeDetail(@PathVariable Long id, Model model) {
        NoticeDto notice = noticeService.findById(id);
        model.addAttribute("notice", notice);
        return "/admin/notice/detail";
    }

    @GetMapping("/regist")
    public String NoticeRegistForm(@ModelAttribute("noticeDto") NoticeDto noticeDto, Model model) {
        if (noticeDto == null) {
            model.addAttribute("noticeDto", new NoticeDto());
        }
        return "/admin/notice/regist";
    }

    @PostMapping("/upload")
    @ResponseBody
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어 있습니다.");
        }

        String domain = "notice";
        String url = imageUpload.uploadAndGetUrl(domain, file);

        return Map.of("url", url);
    }

    @PostMapping("/regist")
    public String noticeRegist(@ModelAttribute NoticeDto noticeDto, Model model, RedirectAttributes redirectAttributes) {
        try {
            noticeService.saveNotice(noticeDto);
            redirectAttributes.addFlashAttribute("successMessage", "공지사항이 등록되었습니다.");
            return "redirect:/admin/notice";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "공지사항 등록 중 오류가 발생했습니다.");
            model.addAttribute("noticeDto", noticeDto);
            return "redirect:/admin/notice/regist";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try{
            noticeService.deleteNotice(id);
            redirectAttributes.addFlashAttribute("successMessage", "해당 공지글을 삭제했습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "공지글 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/notice";
    }
}
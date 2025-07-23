package com.yesul.notice.service;

import com.yesul.exception.handler.AdminNotFoundException;
import com.yesul.notice.model.dto.NoticeDto;
import com.yesul.notice.model.entity.Notice;
import com.yesul.notice.model.enums.NoticeType;
import com.yesul.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    public Page<NoticeDto> findNoticeList(Pageable pageable) {
        Page<Notice> noticeListPageable = noticeRepository.findAll(pageable);
        return noticeListPageable
                .map(notice -> modelMapper.map(notice, NoticeDto.class))
                .map(dto -> {
                    dto.setContent(Jsoup.parse(dto.getContent()).text());
                    return dto;
                });
    }

    public Page<NoticeDto> findNoticeEventList(Pageable pageable) {
        Page<Notice> noticeListPageable = noticeRepository.findByType(pageable, NoticeType.EVENT);
        return noticeListPageable
                .map(notice -> modelMapper.map(notice, NoticeDto.class))
                .map(dto -> {
                    dto.setContent(Jsoup.parse(dto.getContent()).text());
                    return dto;
                });
    }

    public NoticeDto findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("해당 공지사항이 존재하지 않습니다."));
        return modelMapper.map(notice, NoticeDto.class);
    }

    @Transactional
    public void saveNotice(NoticeDto noticeDto) {
        Notice notice = Notice.builder()
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .formId(noticeDto.getFormId())
                .type(noticeDto.getType())
                .imageUrl(noticeDto.getImageUrl())
                .formUrl(noticeDto.getFormUrl())
                .formId(noticeDto.getFormId())
                .point(noticeDto.getPoint())
                .build();
        noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
            .orElseThrow(() -> new AdminNotFoundException("해당 공지사항이 존재하지 않아 삭제할 수 없습니다."));

        noticeRepository.delete(notice);
    }
}

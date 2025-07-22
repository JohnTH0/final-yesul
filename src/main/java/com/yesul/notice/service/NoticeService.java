package com.yesul.notice.service;

import com.yesul.notice.model.dto.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    Page<NoticeDto> findNoticeList(Pageable pageable);

    Page<NoticeDto> findNoticeEventList(Pageable pageable);

    NoticeDto findById(Long id);

    void saveNotice(NoticeDto noticeDto);

    void deleteNotice(Long id);
}

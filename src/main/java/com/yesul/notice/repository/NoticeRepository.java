package com.yesul.notice.repository;

import com.yesul.event.model.entity.EventList;
import com.yesul.notice.model.entity.Notice;
import com.yesul.notice.model.enums.NoticeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findByType(Pageable pageable, NoticeType type);
    Notice findByFormId(String formId);
}

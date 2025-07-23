package com.yesul.notice.repository;

import com.yesul.notice.model.entity.Notice;
import com.yesul.notice.model.enums.NoticeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findByType(Pageable pageable, NoticeType type);
    Notice findByFormId(String formId);
}

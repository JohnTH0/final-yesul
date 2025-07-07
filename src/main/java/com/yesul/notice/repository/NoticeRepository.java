package com.yesul.notice.repository;

import com.yesul.notice.model.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // findAll(), findById(), save(), delete() 등 자동제공 메서드 사용중. (지우지말것)
}

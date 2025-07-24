package com.yesul.event.repository;

import com.yesul.event.model.entity.EventList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventListRepository extends JpaRepository<EventList, String> {
    Page<EventList> findByNoticeId(Pageable pageable, Long noticeId);
}

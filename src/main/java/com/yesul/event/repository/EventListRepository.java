package com.yesul.event.repository;

import com.yesul.event.model.entity.EventList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventListRepository extends JpaRepository<EventList, String> {
}

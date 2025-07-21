package com.yesul.alcohol.repository;

import com.yesul.alcohol.model.dto.AlcoholDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlcoholRepositoryCustom {
    Page<AlcoholDto> findAlcoholList(Pageable pageable);
    List<AlcoholDto> findPopularAlcoholByLikes();
}

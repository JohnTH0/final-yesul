package com.yesul.alcohol.repository;

import com.yesul.alcohol.model.dto.AlcoholDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlcoholRepositoryCustom {
    Page<AlcoholDto> findAlcoholList(Pageable pageable);
}

package com.yesul.like.repository;

import java.util.List;
import java.util.Optional;

import com.yesul.like.model.dto.AlcoholLikeDto;
import com.yesul.like.model.entity.AlcoholLike;

public interface AlcoholLikeRepositoryCustom {
    Optional<AlcoholLike> findByAlcoholAndUser(Long alcoholId, Long userId);
    void deleteByAlcoholAndUser(Long alcoholId, Long userId);
    int countByAlcohol(Long alcoholId);
    List<AlcoholLikeDto> findLikesByUser(Long userId);
}

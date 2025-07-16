package com.yesul.like.service;

import java.util.List;
import com.yesul.like.model.dto.AlcoholLikeDto;

public interface AlcoholLikeService {
    boolean toggleLike(Long alcoholId, Long userId);
    int getLikeCount(Long alcoholId);
    List<AlcoholLikeDto> getLikedAlcohols(Long userId);
}
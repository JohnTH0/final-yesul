package com.yesul.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yesul.like.model.entity.AlcoholLike;

public interface AlcoholLikeRepository
        extends JpaRepository<AlcoholLike, Long>,
        AlcoholLikeRepositoryCustom {
}

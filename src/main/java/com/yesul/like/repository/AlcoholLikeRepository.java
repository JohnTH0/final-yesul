package com.yesul.like.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.yesul.like.model.entity.AlcoholLike;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface AlcoholLikeRepository
        extends JpaRepository<AlcoholLike, Long>,
        AlcoholLikeRepositoryCustom {

    @Query("SELECT al.alcohol.id FROM AlcoholLike al WHERE al.user.id = :userId")
    Set<Long> findLikedAlcoholIdsByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndAlcoholId(Long userId, Long alcoholId);
}

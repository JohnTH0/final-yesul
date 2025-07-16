package com.yesul.like.repository;

import java.util.List;
import java.util.Optional;

import com.yesul.like.model.entity.AlcoholLike;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yesul.like.model.dto.AlcoholLikeDto;
import com.yesul.like.model.entity.QAlcoholLike;
import com.yesul.alcohol.model.entity.QAlcohol;
import com.yesul.user.model.entity.QUser;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AlcoholLikeRepositoryImpl implements AlcoholLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QAlcoholLike al = QAlcoholLike.alcoholLike;
    private final QAlcohol    ao = QAlcohol.alcohol;
    private final QUser       u  = QUser.user;

    @Override
    public Optional<AlcoholLike> findByAlcoholAndUser(Long alcoholId, Long userId) {
        AlcoholLike result = queryFactory
                .selectFrom(al)
                .where(al.alcohol.id.eq(alcoholId)
                        .and(al.user.id.eq(userId)))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public void deleteByAlcoholAndUser(Long alcoholId, Long userId) {
        queryFactory
                .delete(al)
                .where(al.alcohol.id.eq(alcoholId)
                        .and(al.user.id.eq(userId)))
                .execute();
    }

    @Override
    public int countByAlcohol(Long alcoholId) {
        Long cnt = queryFactory
                .select(al.count())
                .from(al)
                .where(al.alcohol.id.eq(alcoholId))
                .fetchOne();
        return cnt == null ? 0 : cnt.intValue();
    }

    @Override
    public List<AlcoholLikeDto> findLikesByUser(Long userId) {
        return queryFactory
                .select(Projections.constructor(
                        AlcoholLikeDto.class,
                        al.alcohol.id,
                        ao.name,
                        ao.image,       // Alcohol.image → DTO thumbnailUrl
                        al.createdAt))
                .from(al)
                .join(al.alcohol, ao)
                .where(al.user.id.eq(userId))
                .orderBy(al.createdAt.desc())
                .fetch();
    }
}

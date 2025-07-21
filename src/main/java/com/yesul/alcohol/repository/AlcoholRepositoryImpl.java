package com.yesul.alcohol.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yesul.alcohol.model.dto.AlcoholDto;
import com.yesul.alcohol.model.entity.QAlcohol;
import com.yesul.like.model.entity.QAlcoholLike;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlcoholRepositoryImpl implements AlcoholRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AlcoholDto> findAlcoholList(Pageable pageable) {
        QAlcohol alcohol = QAlcohol.alcohol;

        List<AlcoholDto> content = queryFactory
                .select(Projections.constructor(AlcoholDto.class,
                        alcohol.id,
                        alcohol.name,
                        alcohol.brand,
                        alcohol.province,
                        alcohol.city,
                        alcohol.type,
                        alcohol.volumeMl,
                        alcohol.abv,
                        alcohol.image))
                .from(alcohol)
                .orderBy(alcohol.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(alcohol.count())
                .from(alcohol)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public List<AlcoholDto> findPopularAlcoholByLikes() {
        QAlcohol alcohol = QAlcohol.alcohol;
        QAlcoholLike alcoholLike = QAlcoholLike.alcoholLike;

        NumberTemplate<Integer> likeCount = Expressions.numberTemplate(
                Integer.class, "count({0})", alcoholLike.id);

        return queryFactory
                .select(Projections.constructor(AlcoholDto.class,
                        alcohol.id,
                        alcohol.name,
                        alcohol.province,
                        alcohol.city,
                        likeCount))
                .from(alcohol)
                .join(alcoholLike).on(alcoholLike.alcohol.eq(alcohol))
                .groupBy(alcohol.id)
                .orderBy(likeCount.desc())
                .fetch();
    }
}

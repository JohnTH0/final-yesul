package com.yesul.community.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yesul.community.model.dto.PostResponseDto;
import com.yesul.like.model.entity.QPostLike;
import com.yesul.community.model.entity.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostResponseDto> findPopularPostsByLikes() {
        QPost post = QPost.post;
        QPostLike like = QPostLike.postLike;

        NumberTemplate<Integer> likeCount = Expressions.numberTemplate(
                Integer.class, "count({0})", like.id);

        return queryFactory
                .select(Projections.constructor(PostResponseDto.class, post.id, post.title, likeCount))
                .from(post)
                .join(like).on(like.post.eq(post))
                .groupBy(post.id)
                .orderBy(likeCount.desc())
                .fetch();
    }

}

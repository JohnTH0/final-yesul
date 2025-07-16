package com.yesul.user.repository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.yesul.user.model.entity.QUser.user;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yesul.user.model.dto.response.UserListResponseDto;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserListResponseDto> findAllUserInfoDto() {
        return queryFactory
                .select(Projections.constructor(
                        UserListResponseDto.class,
                        user.id,
                        user.email,
                        user.nickname,
                        user.birthday,
                        user.status,
                        user.createdAt
                ))
                .from(user)
                .fetch();
    }
}

package com.yesul.chatroom.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yesul.chatroom.model.dto.ChatRoomSummaryResponse;
import com.yesul.chatroom.model.entity.QChatRoom;
import com.yesul.user.model.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatRoomSummaryResponse> findChatRoomsWithCursor(Long cursor, int size) {
        QChatRoom cr = QChatRoom.chatRoom;
        QUser u = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();
        if (cursor != null) {
            builder.and(cr.id.lt(cursor));
        }

        return queryFactory
                .select(Projections.constructor(
                        ChatRoomSummaryResponse.class,
                        cr.id,
                        cr.lastMessage,
                        cr.adminUnreadCount,
                        u.id,
                        u.name,
                        u.profile
                ))
                .from(cr)
                .join(cr.user, u)
                .where(builder)
                .orderBy(cr.id.desc())
                .limit(size)
                .fetch();
    }

    @Override
    public List<ChatRoomSummaryResponse> searchChatRoom(String keyword) {
        QChatRoom cr = QChatRoom.chatRoom;
        QUser u = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();
        if (keyword != null && !keyword.isBlank()) {
            builder.and(u.name.containsIgnoreCase(keyword));
        }

        return queryFactory
                .select(Projections.constructor(
                        ChatRoomSummaryResponse.class,
                        cr.id,
                        cr.lastMessage,
                        cr.adminUnreadCount,
                        u.id,
                        u.name,
                        u.profile
                ))
                .from(cr)
                .join(cr.user, u)
                .where(builder)
                .orderBy(cr.id.desc())
                .fetch();
    }



    @Override
    public int countTotalUnreadCount() {
        QChatRoom cr = QChatRoom.chatRoom;
        return queryFactory
                .select(cr.adminUnreadCount.sum())
                .from(cr)
                .fetchOne();
    }
}


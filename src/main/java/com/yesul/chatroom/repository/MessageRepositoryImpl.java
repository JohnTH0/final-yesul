package com.yesul.chatroom.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yesul.chatroom.model.entity.Message;
import com.yesul.chatroom.model.entity.QMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Message> getMessagesWithCursor(Long chatRoomId, Long lastMessageId, Pageable pageable) {
        QMessage m = QMessage.message;

        List<Message> content = queryFactory
                .selectFrom(m)
                .where(
                        m.chatRoom.id.eq(chatRoomId),
                        m.id.lt(lastMessageId)
                )
                .orderBy(m.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) content.remove(content.size() - 1); // 하나 더 가져온 걸 제거

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<Message> getMessagesFirstPage(Long chatRoomId, Pageable pageable) {
        QMessage m = QMessage.message;

        List<Message> content = queryFactory
                .selectFrom(m)
                .where(m.chatRoom.id.eq(chatRoomId))
                .orderBy(m.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) content.remove(content.size() - 1);

        return new SliceImpl<>(content, pageable, hasNext);
    }

}

package kr.co.pawpaw.mysql.chatroom.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroom;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroomParticipant;
import kr.co.pawpaw.mysql.chatroom.domain.QTrendingChatroom;
import kr.co.pawpaw.mysql.chatroom.dto.QTrendingChatroomResponse;
import kr.co.pawpaw.mysql.chatroom.dto.TrendingChatroomResponse;
import kr.co.pawpaw.mysql.storage.domain.QFile;
import kr.co.pawpaw.mysql.user.domain.QUser;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrendingChatroomCustomRepository {
    private final JPAQueryFactory queryFactory;
    private static final QChatroom qChatroom = QChatroom.chatroom;
    private static final QChatroomParticipant qChatroomParticipant = new QChatroomParticipant("qChatroomParticipant");
    private static final QChatroomParticipant qChatroomParticipantManager = new QChatroomParticipant("qChatroomParticipantManager");
    private static final QUser qUserManager = QUser.user;
    private static final QFile qFileManager = new QFile("qFileManager");
    private static final QTrendingChatroom qTrendingChatroom = QTrendingChatroom.trendingChatroom;

    public Slice<TrendingChatroomResponse> findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(
        final UserId userId,
        final Long beforeId,
        final int size
    ) {
        BooleanExpression condition = qChatroom.id.notIn(
            JPAExpressions.select(qChatroomParticipant.chatroom.id)
                .from(qChatroomParticipant)
                .where(qChatroomParticipant.user.userId.eq(userId)))
            .and(qChatroom.searchable.isTrue());

        if (Objects.nonNull(beforeId)) {
            condition = condition.and(qTrendingChatroom.id.lt(beforeId));
        }

        List<TrendingChatroomResponse> chatroomResponseList = queryFactory.select(
            new QTrendingChatroomResponse(
                qChatroom.id,
                qTrendingChatroom.id,
                qChatroom.name,
                qChatroom.description,
                qChatroom.hashTagList,
                qUserManager.nickname,
                qFileManager.fileUrl,
                qChatroomParticipant.count()
            ))
            .from(qTrendingChatroom)
            .innerJoin(qTrendingChatroom.chatroom, qChatroom)
            .innerJoin(qChatroom.manager, qChatroomParticipantManager)
            .innerJoin(qChatroomParticipantManager.user, qUserManager)
            .innerJoin(qUserManager.userImage, qFileManager)
            .leftJoin(qChatroom.chatroomParticipants, qChatroomParticipant)
            .where(condition)
            .groupBy(qChatroom.id)
            .orderBy(qTrendingChatroom.id.desc())
            .limit(size+1)
            .fetch();

        return new SliceImpl<>(chatroomResponseList.subList(0, Math.min(size, chatroomResponseList.size())), PageRequest.of(0, size), chatroomResponseList.size() > size);
    }
}

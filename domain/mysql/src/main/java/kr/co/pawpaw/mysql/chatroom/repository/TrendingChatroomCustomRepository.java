package kr.co.pawpaw.mysql.chatroom.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroom;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroomHashTag;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroomParticipant;
import kr.co.pawpaw.mysql.chatroom.domain.QTrendingChatroom;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrendingChatroomCustomRepository {
    private final JPAQueryFactory queryFactory;
    private static final QChatroom qChatroom = QChatroom.chatroom;
    private static final QChatroomHashTag qChatroomHashTag = QChatroomHashTag.chatroomHashTag;
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

        condition = qTrendingChatroom.id.in(
            JPAExpressions.select(qTrendingChatroom.id)
                .from(qTrendingChatroom)
                .innerJoin(qTrendingChatroom.chatroom, qChatroom)
                .where(condition)
                .orderBy(qTrendingChatroom.id.desc())
                .limit(size+1)
        );

        List<TrendingChatroomResponse> chatroomResponseList = queryFactory.selectFrom(qTrendingChatroom)
            .innerJoin(qTrendingChatroom.chatroom, qChatroom)
            .innerJoin(qChatroom.manager, qChatroomParticipantManager)
            .innerJoin(qChatroomParticipantManager.user, qUserManager)
            .innerJoin(qUserManager.userImage, qFileManager)
            .leftJoin(qChatroom.chatroomParticipants, qChatroomParticipant)
            .leftJoin(qChatroomHashTag).on(qChatroom.eq(qChatroomHashTag.chatroom))
            .where(condition)
            .transform(
                groupBy(qChatroom.id).as(
                    qTrendingChatroom.id,
                    qChatroom.name,
                    qChatroom.description,
                    set(qChatroomHashTag.hashTag),
                    qUserManager.nickname,
                    qFileManager.fileUrl,
                    set(qChatroomParticipant.id)
                ))
            .entrySet()
            .stream()
            .map(entry -> new TrendingChatroomResponse(
                entry.getKey(),
                entry.getValue().getOne(qTrendingChatroom.id),
                entry.getValue().getOne(qChatroom.name),
                entry.getValue().getOne(qChatroom.description),
                new ArrayList<>(entry.getValue().getSet(qChatroomHashTag.hashTag)),
                entry.getValue().getOne(qUserManager.nickname),
                entry.getValue().getOne(qFileManager.fileUrl),
                (long) entry.getValue().getSet(qChatroomParticipant.id).size()
            )).collect(Collectors.toList());

        return new SliceImpl<>(chatroomResponseList.subList(0, Math.min(size, chatroomResponseList.size())), PageRequest.of(0, size), chatroomResponseList.size() > size);
    }
}

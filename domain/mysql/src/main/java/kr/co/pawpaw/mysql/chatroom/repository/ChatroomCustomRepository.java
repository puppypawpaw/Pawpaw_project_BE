package kr.co.pawpaw.mysql.chatroom.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroom;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroomHashTag;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroomParticipant;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroomSchedule;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomDetailData;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomResponse;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomSimpleResponse;
import kr.co.pawpaw.mysql.chatroom.dto.QChatroomSimpleResponse;
import kr.co.pawpaw.mysql.common.util.QueryUtil;
import kr.co.pawpaw.mysql.storage.domain.QFile;
import kr.co.pawpaw.mysql.user.domain.QUser;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatroomCustomRepository {
    private final JPAQueryFactory queryFactory;
    private static final QChatroom qChatroom = QChatroom.chatroom;
    private static final QChatroomHashTag qChatroomHashTag = QChatroomHashTag.chatroomHashTag;
    private static final QChatroomParticipant myQChatroomParticipant = new QChatroomParticipant("myQChatroomParticipant");
    private static final QChatroomParticipant qChatroomParticipant = new QChatroomParticipant("qChatroomParticipant");
    private static final QChatroomParticipant qChatroomParticipantManager = new QChatroomParticipant("qChatroomParticipantManager");
    private static final QUser qUserManager = QUser.user;
    private static final QUser qUserParticipant = new QUser("qUserParticipant");
    private static final QFile qFileCover = new QFile("qFileCover");
    private static final QFile qFileManager = new QFile("qFileManager");
    private static final QChatroomSchedule qChatroomSchedule = QChatroomSchedule.chatroomSchedule;

    public Slice<ChatroomResponse> findBySearchQuery(
        final String query,
        final UserId userId,
        PageRequest pageRequest
    ) {
        // Full text search
        BooleanBuilder nameFullTextCondition = QueryUtil.fullTextSearchCondition(qChatroom.name, query);
        BooleanBuilder descriptionFullTextCondition = QueryUtil.fullTextSearchCondition(qChatroom.description, query);
        BooleanBuilder hashTagFullTextCondition = QueryUtil.fullTextSearchCondition(qChatroomHashTag.hashTag, query);

        if (Objects.isNull(pageRequest)) pageRequest = PageRequest.of(0, Integer.MAX_VALUE - 1);

        List<ChatroomResponse> result = queryFactory
            .select(
                qChatroom.id,
                qChatroom.name,
                qChatroom.description,
                Expressions.stringTemplate("function('group_concat_distinct',{0})", qChatroomHashTag.hashTag),
                qUserManager.nickname,
                qFileManager.fileUrl,
                Expressions.stringTemplate("function('group_concat_distinct',{0})", qChatroomParticipant.id)
            )
            .from(qChatroom)
            .innerJoin(qChatroom.manager, qChatroomParticipantManager)
            .innerJoin(qChatroomParticipantManager.user, qUserManager)
            .leftJoin(qUserManager.userImage, qFileManager)
            .leftJoin(qChatroom.chatroomParticipants, qChatroomParticipant)
            .leftJoin(qChatroomHashTag).on(qChatroom.eq(qChatroomHashTag.chatroom))
            .where(
                qChatroom.id.notIn(JPAExpressions.select(qChatroomParticipant.chatroom.id)
                        .from(qChatroomParticipant)
                        .where(qChatroomParticipant.user.userId.eq(userId)))
                    .and(nameFullTextCondition
                        .or(descriptionFullTextCondition)
                        .or(qChatroom.in(JPAExpressions.select(qChatroomHashTag.chatroom)
                            .from(qChatroomHashTag)
                            .where(hashTagFullTextCondition))))
            )
            .groupBy(qChatroom.id)
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize() + 1)
            .fetch()
            .stream()
            .map(tuple -> new ChatroomResponse(
                tuple.get(0, Long.class),
                tuple.get(1, String.class),
                tuple.get(2, String.class),
                splitIfNonNull(tuple.get(3, String.class)),
                tuple.get(4, String.class),
                tuple.get(5, String.class),
                countIfNonNull(splitIfNonNull(tuple.get(6, String.class)))
            ))
            .collect(Collectors.toList());

        return new SliceImpl<>(result.subList(0, Math.min(result.size(), pageRequest.getPageSize())), pageRequest, result.size() > pageRequest.getPageSize());
    }

    private static Collection<String> splitIfNonNull(final String str) {
        return Objects.nonNull(str) ? Arrays.asList(str.split(",")) : null;
    }

    private static long countIfNonNull(final Collection<?> collection) {
        return Objects.nonNull(collection) ? (long) collection.size() : 0;
    }

    public List<ChatroomDetailData> findAllByUserIdWithDetailData(final UserId userId) {
        return queryFactory
            .from(myQChatroomParticipant)
            .innerJoin(myQChatroomParticipant.chatroom, qChatroom)
            .leftJoin(qChatroom.coverFile, qFileCover)
            .innerJoin(qChatroom.manager, qChatroomParticipantManager)
            .innerJoin(qChatroomParticipantManager.user, qUserManager)
            .leftJoin(qUserManager.userImage, qFileManager)
            .leftJoin(qChatroomParticipant).on(qChatroom.eq(qChatroomParticipant.chatroom))
            .leftJoin(qChatroomSchedule).on(qChatroom.eq(qChatroomSchedule.chatroom)
                .and(qChatroomSchedule.endDate.after(LocalDateTime.now())))
            .leftJoin(qChatroomHashTag).on(qChatroom.eq(qChatroomHashTag.chatroom))
            .where(myQChatroomParticipant.user.userId.eq(Expressions.constant(userId)))
            .transform(groupBy(qChatroom.id)
                .as(
                    qChatroom.name,
                    qChatroom.description,
                    qFileCover.fileUrl,
                    set(qChatroomHashTag.hashTag),
                    qUserManager.nickname,
                    qFileManager.fileUrl,
                    set(qChatroomParticipant.id),
                    set(qChatroomSchedule.id)
                )).entrySet()
            .stream()
            .map(entry -> new ChatroomDetailData(
                entry.getKey(),
                entry.getValue().getOne(qChatroom.name),
                entry.getValue().getOne(qChatroom.description),
                entry.getValue().getOne(qFileCover.fileUrl),
                new ArrayList<String>(entry.getValue().getSet(qChatroomHashTag.hashTag)),
                entry.getValue().getOne(qUserManager.nickname),
                entry.getValue().getOne(qFileManager.fileUrl),
                (long) entry.getValue().getSet(qChatroomParticipant.id).size(),
                false,
                !entry.getValue().getSet(qChatroomSchedule.id).isEmpty()
            ))
            .collect(Collectors.toList());
    }

    public List<ChatroomResponse> findAccessibleNewChatroomByUserId(final UserId userId) {
         BooleanExpression condition = qChatroom.id.in(
             JPAExpressions.select(qChatroom.id)
                .from(qChatroom)
                .where(qChatroom.id.notIn(
                        JPAExpressions.select(qChatroomParticipant.chatroom.id)
                            .from(qChatroomParticipant)
                            .where(qChatroomParticipant.user.userId.eq(userId)))
                    .and(qChatroom.searchable.isTrue()))
                 .limit(10)
         );

        return queryFactory
            .from(qChatroom)
            .innerJoin(qChatroom.manager, qChatroomParticipantManager)
            .innerJoin(qChatroomParticipantManager.user, qUserManager)
            .innerJoin(qUserManager.userImage, qFileManager)
            .leftJoin(qChatroom.chatroomParticipants, qChatroomParticipant)
            .leftJoin(qChatroomHashTag).on(qChatroom.eq(qChatroomHashTag.chatroom))
            .where(condition)
            .transform(
                groupBy(qChatroom.id).as(
                    qChatroom.name,
                    qChatroom.description,
                    set(qChatroomHashTag.hashTag),
                    qUserManager.nickname,
                    qFileManager.fileUrl,
                    set(qChatroomParticipant.id)
                ))
            .entrySet().stream()
            .map(entry -> new ChatroomResponse(
                entry.getKey(),
                entry.getValue().getOne(qChatroom.name),
                entry.getValue().getOne(qChatroom.description),
                new ArrayList<String>(entry.getValue().getSet(qChatroomHashTag.hashTag)),
                entry.getValue().getOne(qUserManager.nickname),
                entry.getValue().getOne(qFileManager.fileUrl),
                (long) entry.getValue().getSet(qChatroomParticipant.id).size()
            ))
            .sorted(Comparator.comparingDouble(e -> Math.random()))
            .collect(Collectors.toList());
    }

    public ChatroomSimpleResponse findByChatroomIdAsSimpleResponse(final Long chatroomId) {
        return queryFactory.select(
            new QChatroomSimpleResponse(
                qChatroom.name,
                qChatroom.description,
                qChatroomParticipant.countDistinct(),
                qFileCover.fileUrl
            ))
            .from(qChatroom)
            .leftJoin(qChatroom.coverFile, qFileCover)
            .leftJoin(qChatroomParticipant).on(qChatroom.eq(qChatroomParticipant.chatroom))
            .where(qChatroom.id.eq(chatroomId))
            .fetchOne();
    }
}
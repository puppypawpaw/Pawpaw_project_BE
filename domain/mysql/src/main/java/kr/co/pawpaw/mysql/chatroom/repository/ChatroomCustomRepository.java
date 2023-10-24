package kr.co.pawpaw.mysql.chatroom.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.Group;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
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
import kr.co.pawpaw.mysql.storage.domain.QFile;
import kr.co.pawpaw.mysql.user.domain.QUser;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

@Slf4j
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
    private static final QFile qFileCover = new QFile("qFileCover");
    private static final QFile qFileManager = new QFile("qFileManager");
    private static final QChatroomSchedule qChatroomSchedule = QChatroomSchedule.chatroomSchedule;

    public List<ChatroomResponse> findBySearchQuery(final String query) {
        // Full text search
        BooleanBuilder nameFullTextCondition = fullTextSearchCondition(qChatroom.name, query);
        BooleanBuilder descriptionFullTextCondition = fullTextSearchCondition(qChatroom.description, query);
        BooleanBuilder hashTagFullTextCondition = fullTextSearchCondition(qChatroomHashTag.hashTag, query);

        return queryFactory
            .from(qChatroom)
            .innerJoin(qChatroom.manager, qChatroomParticipantManager)
            .innerJoin(qChatroomParticipantManager.user, qUserManager)
            .leftJoin(qUserManager.userImage, qFileManager)
            .leftJoin(qChatroom.chatroomParticipants, qChatroomParticipant)
            .leftJoin(qChatroomHashTag).on(qChatroom.eq(qChatroomHashTag.chatroom))
            .where(nameFullTextCondition
                .or(descriptionFullTextCondition)
                .or(qChatroom.in(JPAExpressions.select(qChatroomHashTag.chatroom)
                    .from(qChatroomHashTag)
                    .where(hashTagFullTextCondition)))
            )
            .transform(groupBy(qChatroom.id)
                .as(
                    qChatroom.name,
                    qChatroom.description,
                    set(qChatroomHashTag.hashTag),
                    qUserManager.nickname,
                    qFileManager.fileUrl,
                    set(qChatroomParticipant.id)
                )).entrySet()
            .stream()
            .map(entry -> {
                Group value = entry.getValue();
                return new ChatroomResponse(
                    entry.getKey(),
                    value.getOne(qChatroom.name),
                    value.getOne(qChatroom.description),
                    new ArrayList<>(value.getSet(qChatroomHashTag.hashTag)),
                    value.getOne(qUserManager.nickname),
                    value.getOne(qFileManager.fileUrl),
                    (long) value.getSet(qChatroomParticipant.id).size()
                );
            })
            .collect(Collectors.toList());
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

    private BooleanBuilder fullTextSearchCondition(StringPath qPath, String query) {
        return new BooleanBuilder()
            .and(Expressions.numberTemplate(
                Double.class,
                "function('match',{0},{1})", qPath, query
            ).gt(0));
    }
}
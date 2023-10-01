package kr.co.pawpaw.mysql.chatroom.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroomSchedule;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroomScheduleParticipant;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomScheduleData;
import kr.co.pawpaw.mysql.chatroom.dto.QChatroomScheduleData;
import kr.co.pawpaw.mysql.chatroom.dto.QChatroomScheduleParticipantResponse;
import kr.co.pawpaw.mysql.storage.domain.QFile;
import kr.co.pawpaw.mysql.user.domain.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

@Repository
@RequiredArgsConstructor
public class ChatroomScheduleCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QChatroomScheduleParticipant qChatroomScheduleParticipant = QChatroomScheduleParticipant.chatroomScheduleParticipant;
    private final QUser qParticipantUser = QUser.user;
    private final QFile qParticipantUserImage = QFile.file;
    private final QChatroomSchedule qChatroomSchedule = QChatroomSchedule.chatroomSchedule;

    public List<ChatroomScheduleData> findNotEndChatroomScheduleByChatroomId(final Long chatroomId) {
        return queryFactory
            .from(qChatroomSchedule)
            .leftJoin(qChatroomScheduleParticipant).on(qChatroomScheduleParticipant.chatroomSchedule.eq(qChatroomSchedule))
            .leftJoin(qChatroomScheduleParticipant.user, qParticipantUser)
            .leftJoin(qParticipantUser.userImage, qParticipantUserImage)
            .where(qChatroomSchedule.chatroom.id.eq(chatroomId)
                .and(qChatroomSchedule.endDate.after(LocalDateTime.now())))
            .orderBy(qChatroomSchedule.startDate.asc())
            .transform(
                groupBy(qChatroomSchedule.id).list(
                    new QChatroomScheduleData(
                        qChatroomSchedule.id,
                        qChatroomSchedule.name,
                        qChatroomSchedule.description,
                        qChatroomSchedule.startDate,
                        qChatroomSchedule.endDate,
                        set(
                            new QChatroomScheduleParticipantResponse(
                                qParticipantUser.nickname,
                                qParticipantUserImage.fileUrl
                            ).skipNulls()
                        )
                    )
                )
            );
    }
}

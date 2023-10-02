package kr.co.pawpaw.mysql.chatroom.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroomParticipant;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomParticipantResponse;
import kr.co.pawpaw.mysql.chatroom.dto.QChatroomParticipantResponse;
import kr.co.pawpaw.mysql.storage.domain.QFile;
import kr.co.pawpaw.mysql.user.domain.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatroomParticipantCustomRepository {
    private final JPAQueryFactory queryFactory;
    private static final QChatroomParticipant qChatroomParticipant = QChatroomParticipant.chatroomParticipant;
    private static final QUser qUser = QUser.user;
    private static final QFile qFile = QFile.file;

    public List<ChatroomParticipantResponse> getChatroomParticipantResponseList(final Long chatroomId) {
        return queryFactory.select(
            new QChatroomParticipantResponse(
                qUser.nickname,
                qUser.briefIntroduction,
                qFile.fileUrl,
                qChatroomParticipant.role
            ))
            .from(qChatroomParticipant)
            .innerJoin(qChatroomParticipant.user, qUser)
            .leftJoin(qUser.userImage, qFile)
            .where(qChatroomParticipant.chatroom.id.eq(chatroomId))
            .fetch();
    }
}

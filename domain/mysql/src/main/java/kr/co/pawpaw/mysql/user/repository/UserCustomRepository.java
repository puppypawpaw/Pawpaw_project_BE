package kr.co.pawpaw.mysql.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroomParticipant;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomNonParticipantResponse;
import kr.co.pawpaw.mysql.chatroom.dto.QChatroomNonParticipantResponse;
import kr.co.pawpaw.mysql.storage.domain.QFile;
import kr.co.pawpaw.mysql.user.domain.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCustomRepository {
    private final JPAQueryFactory queryFactory;
    private static final QUser qUser = QUser.user;
    private static final QFile qFile = QFile.file;
    private static final QChatroomParticipant qChatroomParticipant = QChatroomParticipant.chatroomParticipant;

    public List<ChatroomNonParticipantResponse> searchChatroomNonParticipant(
        final Long chatroomId,
        final String nicknameSearchKeyword
    ) {
        return queryFactory.select(new QChatroomNonParticipantResponse(
                qUser.userId,
                qUser.nickname,
                qUser.briefIntroduction,
                qFile.fileUrl
            ))
            .from(qUser)
            .leftJoin(qUser.userImage, qFile)
            .where(qUser.userId.notIn(
                select(qChatroomParticipant.user.userId)
                    .from(qChatroomParticipant)
                    .where(qChatroomParticipant.chatroom.id.eq(chatroomId)))
                .and(qUser.nickname.contains(nicknameSearchKeyword))
            )
            .fetch();
    }
}

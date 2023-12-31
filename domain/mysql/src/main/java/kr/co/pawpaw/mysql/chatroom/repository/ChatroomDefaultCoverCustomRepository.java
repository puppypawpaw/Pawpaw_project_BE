package kr.co.pawpaw.mysql.chatroom.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.chatroom.domain.QChatroomDefaultCover;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomCoverResponse;
import kr.co.pawpaw.mysql.chatroom.dto.QChatroomCoverResponse;
import kr.co.pawpaw.mysql.storage.domain.QFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatroomDefaultCoverCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private static final QChatroomDefaultCover qChatroomDefaultCover = QChatroomDefaultCover.chatroomDefaultCover;
    private static final QFile qFile = QFile.file;

    public List<ChatroomCoverResponse> findAllChatroomCover() {
        return jpaQueryFactory.select(new QChatroomCoverResponse(
                qChatroomDefaultCover.id,
                qFile.fileUrl
            ))
            .from(qChatroomDefaultCover)
            .innerJoin(qChatroomDefaultCover.coverFile, qFile)
            .fetch();
    }
}

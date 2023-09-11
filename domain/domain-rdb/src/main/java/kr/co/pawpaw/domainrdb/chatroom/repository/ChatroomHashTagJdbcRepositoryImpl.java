package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomHashTag;
import kr.co.pawpaw.domainrdb.common.repository.JdbcBatch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatroomHashTagJdbcRepositoryImpl implements ChatroomHashTagJdbcRepository {
    private final JdbcBatch<ChatroomHashTag> jdbcBatch;

    @Override
    public List<Long> saveAll(final List<ChatroomHashTag> chatroomHashTagCollection) {
        return jdbcBatch.batchInsert(
            "INSERT INTO chatroom_hash_tag (created_date, modified_date, hash_tag, chatroom_id) VALUES(?,?,?,?)",
            chatroomHashTagCollection, (ps, entity) -> {
            LocalDateTime now = LocalDateTime.now();

            ps.setObject(1, now);
            ps.setObject(2, now);
            ps.setObject(3, entity.getHashTag());
            ps.setObject(4, entity.getChatroom().getId());
            ps.addBatch();
        });
    }
}

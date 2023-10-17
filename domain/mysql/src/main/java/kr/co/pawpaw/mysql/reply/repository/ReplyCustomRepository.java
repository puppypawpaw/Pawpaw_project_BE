package kr.co.pawpaw.mysql.reply.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.reply.domain.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.co.pawpaw.mysql.reply.domain.QReply.reply;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyCustomRepository {

    private final JPAQueryFactory query;

    public Slice<Reply> findReplyListByBoardId(Long boardId, Pageable pageable) {
        List<Reply> replies = query.selectFrom(reply)
                .leftJoin(reply.parent).fetchJoin()
                .leftJoin(reply.child).fetchJoin()
                .where(reply.board.id.eq(boardId))
                .orderBy(
                        reply.parent.id.asc().nullsFirst(),
                        reply.createdDate.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = replies.size() > pageable.getPageSize();
        if (hasNext)
            replies.remove(pageable.getPageSize());

        return new SliceImpl<>(replies, pageable, hasNext);
    }
}
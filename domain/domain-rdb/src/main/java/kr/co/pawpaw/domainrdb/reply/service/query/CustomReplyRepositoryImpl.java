package kr.co.pawpaw.domainrdb.reply.service.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.domainrdb.reply.domain.Reply;
import kr.co.pawpaw.domainrdb.reply.repository.CustomReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static kr.co.pawpaw.domainrdb.reply.domain.QReply.reply;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomReplyRepositoryImpl implements CustomReplyRepository {


    private final JPAQueryFactory query;

    @Autowired
    public CustomReplyRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Slice<Reply> findReplyListByBoardId(Long boardId, Pageable pageable) {
        List<Reply> replies = query.selectFrom(reply)
                .leftJoin(reply.parent).fetchJoin()
                .leftJoin(reply.child).fetchJoin()
                .where(reply.board.id.eq(boardId))
                .orderBy(
                        reply.parent.id.asc().nullsFirst(), // 부모댓글을 최상위로
                        reply.createdDate.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getOffset() + 1)
                .fetch();

        boolean hasNext = replies.size() > pageable.getPageSize();
        if (hasNext) {
            // 마지막 하나는 다음 페이지로 넘어갈 데이터이므로 제거
            replies.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(replies, pageable, hasNext);
    }
}

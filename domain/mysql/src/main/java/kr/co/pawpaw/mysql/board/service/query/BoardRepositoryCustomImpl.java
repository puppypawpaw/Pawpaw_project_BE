package kr.co.pawpaw.mysql.board.service.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.board.repository.BoardRepositoryCustom;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static kr.co.pawpaw.mysql.board.domain.QBoard.board;
import static kr.co.pawpaw.mysql.reply.domain.QReply.reply;
import static kr.co.pawpaw.mysql.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public BoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<Board> getBoardListWithRepliesBy(Pageable pageable) {
        List<Board> boards = queryFactory.selectFrom(board)
                .leftJoin(board.user, user).fetchJoin()
                .leftJoin(board.reply, reply).fetchJoin()
                .orderBy(
                        board.createdDate.desc(),
                        reply.createdDate.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getOffset() + 1)
                .fetch();

        boolean hasNext = boards.size() > pageable.getPageSize();
        if (hasNext) {
            // 마지막 하나는 다음 페이지로 넘어갈 데이터이므로 제거
            boards.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(boards, pageable, hasNext);
    }

    @Override
    public Slice<Board> getBoardListWithRepliesByUser_UserId(Pageable pageable, UserId userId) {
        List<Board> boards = queryFactory.selectFrom(board)
                .leftJoin(board.user, user).fetchJoin()
                .leftJoin(board.reply, reply).fetchJoin()
                .orderBy(
                        board.createdDate.desc(),
                        reply.createdDate.asc()
                )
                .where(board.user.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getOffset() + 1)
                .fetch();

        boolean hasNext = boards.size() > pageable.getPageSize();
        if (hasNext)
            boards.remove(pageable.getPageSize());

        return new SliceImpl<>(boards, pageable, hasNext);
    }
}

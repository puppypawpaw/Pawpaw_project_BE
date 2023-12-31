package kr.co.pawpaw.mysql.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static kr.co.pawpaw.mysql.board.domain.QBoard.board;
import static kr.co.pawpaw.mysql.reply.domain.QReply.reply;
import static kr.co.pawpaw.mysql.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCustomRepository {

    private final JPAQueryFactory queryFactory;


    public Slice<Board> getBoardListWithRepliesBy(Pageable pageable) {
        List<Board> boards = queryFactory.selectFrom(board)
                .leftJoin(board.user, user).fetchJoin()
                .leftJoin(board.reply, reply).fetchJoin()
                .where(board.reportedCount.lt(5))
                .orderBy(
                        board.createdDate.desc(),
                        reply.createdDate.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = boards.size() > pageable.getPageSize();
        if (hasNext)
            boards.remove(pageable.getPageSize());

        return new SliceImpl<>(boards, pageable, hasNext);
    }

    public Board getBoardWithRepliesBy(long boardId) {
        return queryFactory.selectFrom(board)
                .leftJoin(board.user, user).fetchJoin()
                .leftJoin(board.reply, reply).fetchJoin()
                .where(board.id.eq(boardId))
                .where(board.reportedCount.lt(5))
                .orderBy(
                        board.createdDate.desc(),
                        reply.createdDate.asc()
                )
                .fetchOne();
    }

    public Slice<Board> getBoardListWithRepliesByUser_UserId(Pageable pageable, UserId userId) {
        List<Board> boards = queryFactory.selectFrom(board)
                .leftJoin(board.user, user).fetchJoin()
                .leftJoin(board.reply, reply).fetchJoin()
                .where(board.reportedCount.lt(5))
                .orderBy(
                        board.createdDate.desc(),
                        reply.createdDate.asc()
                )
                .where(board.user.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = boards.size() > pageable.getPageSize();
        if (hasNext)
            boards.remove(pageable.getPageSize());

        return new SliceImpl<>(boards, pageable, hasNext);
    }

    public Slice<Board> getBoardListWithRepliesBySearch(Pageable pageable, String searchQuery) {
        List<Board> boards = queryFactory.selectFrom(board)
                .leftJoin(board.user, user).fetchJoin()
                .leftJoin(board.reply, reply).fetchJoin()
                .where(board.reportedCount.lt(5))
                .where(searchBySearchQuery(searchQuery))
                .orderBy(
                        board.createdDate.desc(),
                        reply.createdDate.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = boards.size() > pageable.getPageSize();
        if (hasNext)
            boards.remove(pageable.getPageSize());

        return new SliceImpl<>(boards, pageable, hasNext);
    }

    private BooleanExpression searchBySearchQuery(String searchQuery) {
        if (StringUtils.hasText(searchQuery)) {
            return board.content.contains(searchQuery);
        }
        return null;
    }
}

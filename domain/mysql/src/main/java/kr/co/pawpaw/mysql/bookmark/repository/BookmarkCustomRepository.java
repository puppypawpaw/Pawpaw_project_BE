package kr.co.pawpaw.mysql.bookmark.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.bookmark.domain.Bookmark;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.co.pawpaw.mysql.board.domain.QBoard.board;
import static kr.co.pawpaw.mysql.bookmark.domain.QBookmark.bookmark;
import static kr.co.pawpaw.mysql.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<Bookmark> getBoardListWithRepliesByUser_UserId(Pageable pageable, UserId userId) {
        List<Bookmark> boards = queryFactory.selectFrom(bookmark)
                .leftJoin(bookmark.board, board).fetchJoin()
                .leftJoin(bookmark.user, user).fetchJoin()
                .orderBy(
                        board.createdDate.desc()
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
}

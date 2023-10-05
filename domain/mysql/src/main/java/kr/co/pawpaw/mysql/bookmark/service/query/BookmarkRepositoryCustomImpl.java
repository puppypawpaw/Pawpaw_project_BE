package kr.co.pawpaw.mysql.bookmark.service.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.mysql.bookmark.domain.Bookmark;
import kr.co.pawpaw.mysql.bookmark.repository.BookmarkRepositoryCustom;
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
import static kr.co.pawpaw.mysql.bookmark.domain.QBookmark.bookmark;
import static kr.co.pawpaw.mysql.reply.domain.QReply.reply;
import static kr.co.pawpaw.mysql.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookmarkRepositoryCustomImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public BookmarkRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<Bookmark> getBoardListWithRepliesByUser_UserId(Pageable pageable, UserId userId) {
        List<Bookmark> boards = queryFactory.selectFrom(bookmark)
                .leftJoin(bookmark.board, board).fetchJoin()
                .leftJoin(bookmark.user, user).fetchJoin()
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
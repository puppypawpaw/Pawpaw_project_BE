package kr.co.pawpaw.domainrdb.board.service.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.board.repository.CustomBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static kr.co.pawpaw.domainrdb.board.domain.QBoard.board;
import static kr.co.pawpaw.domainrdb.reply.domain.QReply.reply;
import static kr.co.pawpaw.domainrdb.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final JPAQueryFactory query;

    @Autowired
    public CustomBoardRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Slice<Board> getBoardListWithRepliesBy(Pageable pageable) {
        List<Board> boards = query.selectFrom(board)
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
}

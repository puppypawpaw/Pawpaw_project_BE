package kr.co.pawpaw.mysql.board.repository;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardRepositoryCustom {

    Slice<Board> getBoardListWithRepliesBy(Pageable pageable);

    Slice<Board> getBoardListWithRepliesByUser_UserId(Pageable pageable, UserId userId);
}
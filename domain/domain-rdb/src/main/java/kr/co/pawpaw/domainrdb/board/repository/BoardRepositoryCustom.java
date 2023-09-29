package kr.co.pawpaw.domainrdb.board.repository;

import kr.co.pawpaw.domainrdb.board.domain.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardRepositoryCustom {

    Slice<Board> getBoardListWithRepliesBy(Pageable pageable);
}
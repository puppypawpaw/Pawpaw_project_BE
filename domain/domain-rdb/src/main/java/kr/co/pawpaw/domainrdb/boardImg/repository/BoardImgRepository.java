package kr.co.pawpaw.domainrdb.boardImg.repository;

import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.boardImg.domain.BoardImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {
    @Query("select b from BoardImg b where b.board =:board")
    List<BoardImg> findBoardImgsByBoard(@Param("board") Board board);
}

package kr.co.pawpaw.domainrdb.boardImg.repository;

import kr.co.pawpaw.domainrdb.boardImg.domain.BoardImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {

    @Query("SELECT bi FROM BoardImg bi JOIN FETCH bi.file WHERE bi.board.id = :boardId")
    public List<BoardImg> findBoardImgsWithFileByBoardId(@Param("boardId") Long boardId);
}

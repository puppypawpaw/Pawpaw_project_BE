package kr.co.pawpaw.mysql.board.repository;

import kr.co.pawpaw.mysql.board.domain.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = "fileUrls")
    Board findBoardWithFileUrlsById(Long id);
}

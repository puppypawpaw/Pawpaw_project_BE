package kr.co.pawpaw.mysql.board.repository;

import kr.co.pawpaw.mysql.board.domain.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    @Query("SELECT b FROM Board b JOIN b.user u LEFT JOIN b.reply r WHERE (b.content) LIKE LOWER(CONCAT('%', :query, '%'))")
    Slice<Board> searchBoardsByQuery(@Param("query") String query, Pageable pageable);
}

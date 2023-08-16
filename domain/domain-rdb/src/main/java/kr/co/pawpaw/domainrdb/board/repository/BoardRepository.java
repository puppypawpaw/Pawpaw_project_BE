package kr.co.pawpaw.domainrdb.board.repository;

import kr.co.pawpaw.domainrdb.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}

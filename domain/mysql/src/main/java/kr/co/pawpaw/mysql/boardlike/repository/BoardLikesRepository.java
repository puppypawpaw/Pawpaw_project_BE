package kr.co.pawpaw.mysql.boardlike.repository;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.boardlike.domain.BoardLikes;
import kr.co.pawpaw.mysql.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {

    Optional<BoardLikes> deleteBoardLikesByUserAndBoard(User user, Board board);
    boolean existsByUserAndBoard(User user, Board board);

}

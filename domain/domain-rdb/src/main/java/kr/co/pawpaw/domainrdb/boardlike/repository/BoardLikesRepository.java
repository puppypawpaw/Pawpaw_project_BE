package kr.co.pawpaw.domainrdb.boardlike.repository;

import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.boardlike.domain.BoardLikes;
import kr.co.pawpaw.domainrdb.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {

    Optional<BoardLikes> deleteBoardLikesByUserAndBoard(User user, Board board);
    boolean existsByUserAndBoard(User user, Board board);

}

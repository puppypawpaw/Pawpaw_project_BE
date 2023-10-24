package kr.co.pawpaw.mysql.bookmark.repository;


import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.bookmark.domain.Bookmark;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> deleteBookmarkByUserAndBoard(User user, Board board);
    boolean existsByUser_UserIdAndBoard(UserId userId, Board board);
}

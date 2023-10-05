package kr.co.pawpaw.mysql.bookmark.repository;


import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.bookmark.domain.Bookmark;
import kr.co.pawpaw.mysql.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom {

    Optional<Bookmark> deleteBookmarkByUserAndBoard(User user, Board board);
    boolean existsByUserAndBoard(User user, Board board);
}

package kr.co.pawpaw.mysql.bookmark.service.query;


import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.bookmark.domain.Bookmark;
import kr.co.pawpaw.mysql.bookmark.repository.BookmarkRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookmarkQuery {

    private final BookmarkRepository bookmarkRepository;

    public Optional<Bookmark> deleteBookmarkByUserAndBoard(User user, Board board){
        return bookmarkRepository.deleteBookmarkByUserAndBoard(user, board);
    }

    public boolean existsByUserAndBoard(User user, Board board){
        return bookmarkRepository.existsByUserAndBoard(user, board);
    }

    public Slice<Bookmark> getBoardListWithRepliesByUser_UserId(Pageable pageable, UserId userId){
        return bookmarkRepository.getBoardListWithRepliesByUser_UserId(pageable, userId);
    }
}
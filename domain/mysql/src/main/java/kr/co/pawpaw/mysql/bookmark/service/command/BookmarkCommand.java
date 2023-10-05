package kr.co.pawpaw.mysql.bookmark.service.command;


import kr.co.pawpaw.mysql.bookmark.domain.Bookmark;
import kr.co.pawpaw.mysql.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkCommand {

    private final BookmarkRepository bookmarkRepository;

    public Bookmark save(Bookmark bookmark) {
        return bookmarkRepository.save(bookmark);
    }
}

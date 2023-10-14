package kr.co.pawpaw.mysql.bookmark.repository;

import kr.co.pawpaw.mysql.bookmark.domain.Bookmark;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookmarkRepositoryCustom {

    Slice<Bookmark> getBoardListWithRepliesByUser_UserId(Pageable pageable, UserId userId);
}
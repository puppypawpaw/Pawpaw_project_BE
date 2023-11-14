package kr.co.pawpaw.api.service.bookmark;

import kr.co.pawpaw.api.dto.board.BoardDto.BoardListDto;
import kr.co.pawpaw.api.dto.reply.ReplyDto;
import kr.co.pawpaw.api.service.boardImg.BoardImgService;
import kr.co.pawpaw.api.service.boardlike.BoardLikeService;
import kr.co.pawpaw.api.service.reply.ReplyService;
import kr.co.pawpaw.common.exception.board.BoardException;
import kr.co.pawpaw.common.exception.board.BookmarkException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.board.service.query.BoardQuery;
import kr.co.pawpaw.mysql.bookmark.domain.Bookmark;
import kr.co.pawpaw.mysql.bookmark.service.command.BookmarkCommand;
import kr.co.pawpaw.mysql.bookmark.service.query.BookmarkQuery;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final BoardQuery boardQuery;
    private final BookmarkQuery bookmarkQuery;
    private final UserQuery userQuery;
    private final BookmarkCommand bookmarkCommand;
    private final ReplyService replyService;
    private final BoardImgService imgService;
    private final BoardLikeService boardLikeService;


    @Transactional
    public boolean addBookmark(Long boardId, UserId userId) {
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        Board board = boardQuery.findById(boardId).orElseThrow(BoardException.BoardNotFoundException::new);

        if (!existsByUser_UserIdAndBoard(userId, board)) {
            bookmarkCommand.save(new Bookmark(board, user));
            board.addBookmark();
            return true;
        }
        throw new BookmarkException.BookmarkRegistrationFailException();
    }

    @Transactional
    public boolean deleteBookmark(Long boardId, UserId userId) {
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        Board board = boardQuery.findById(boardId).orElseThrow(BoardException.BoardNotFoundException::new);

        if (existsByUser_UserIdAndBoard(userId, board)) {
            bookmarkQuery.deleteBookmarkByUserAndBoard(user, board);
            board.deleteBookmark();
            return true;
        }
        throw new BookmarkException.BookmarkDeleteFailException();
    }
    @Transactional(readOnly = true)
    public Slice<BoardListDto> getBoardListWithRepliesByUser_UserId(Pageable pageable, UserId userId){
        userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        Slice<Bookmark> boardListWithRepliesByUser_UserId = bookmarkQuery.getBoardListWithRepliesByUser_UserIdAndBookmarked(pageable, userId);

        List<BoardListDto> boardListDtos = getBoardListDtos(userId, pageable, boardListWithRepliesByUser_UserId);
        return new SliceImpl<>(boardListDtos, pageable, boardListWithRepliesByUser_UserId.hasNext());
    }

    public boolean existsByUser_UserIdAndBoard(UserId userId, Board board) {
        return bookmarkQuery.existsByUser_UserIdAndBoard(userId, board);
    }

    private List<BoardListDto> getBoardListDtos(UserId userId, Pageable pageable, Slice<Bookmark> bookmarks) {
        List<BoardListDto> boardListDtos = bookmarks.stream()
                .map(bookmark -> convertBoardToDto(userId, bookmark.getBoard()))
                .collect(Collectors.toList());

        boardListDtos.forEach(boardDto -> {
            List<ReplyDto.ReplyListDto> replyList = replyService.findReplyListByBoardId(userId, boardDto.getId(), pageable).getContent();
            boardDto.setReplyListToBoard(replyList);

            Board board = boardQuery.findBoardWithFileUrlsById(boardDto.getId());
            boardDto.setFileUrlsToBoard(board.getFileUrls());
        });
        return boardListDtos;
    }

    private BoardListDto convertBoardToDto(UserId userId, Board board) {
        boolean existBoardLike = boardLikeService.checkLikeExist(userId, board);
        boolean existBookmark = existsByUser_UserIdAndBoard(userId, board);
        String imageUrl = board.getUser().getUserImage().getFileUrl();

        return BoardListDto.builder()
                .userId(board.getUser().getUserId())
                .id(board.getId())
                .content(board.getContent())
                .likedCount(board.getLikedCount())
                .replyCount(board.getReplyCount())
                .userImageUrl(imageUrl)
                .boardLiked(existBoardLike)
                .bookmarked(existBookmark)
                .writer(board.getWriter())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }
}

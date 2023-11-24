package kr.co.pawpaw.api.service.boardlike;

import kr.co.pawpaw.common.exception.board.BoardException.BoardNotFoundException;
import kr.co.pawpaw.common.exception.board.BoardLikeException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.boardlike.domain.BoardLikes;
import kr.co.pawpaw.mysql.board.service.query.BoardQuery;
import kr.co.pawpaw.mysql.boardlike.service.query.BoardLikeQuery;
import kr.co.pawpaw.mysql.boardlike.service.command.BoardLikeCommand;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final BoardLikeQuery boardLikeQuery;
    private final BoardLikeCommand boardLikeCommand;
    private final BoardQuery boardQuery;
    private final UserQuery userQuery;

    @Transactional
    public boolean addLike(Long boardId, UserId userId) {
        Board board = boardQuery.findById(boardId).orElseThrow(BoardNotFoundException::new);
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        if (!checkLikeExist(userId, board)) {
            boardLikeCommand.save(new BoardLikes(user, board));
            board.plusLikedCount();
            return true;
        }
        throw new BoardLikeException.BoardLikeFailException();
    }

    @Transactional
    public boolean deleteLike(Long boardId, UserId userId) {
        Board board = boardQuery.findById(boardId).orElseThrow(BoardNotFoundException::new);
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        if (checkLikeExist(userId, board)) {
            boardLikeQuery.deleteBoardLikesByUserAndBoard(user, board);
            board.minusLikedCount();
            return true;
        }
        throw new BoardLikeException.BoardDeleteLikeFailException();
    }

    public boolean checkLikeExist(UserId userId, Board board) {
        return boardLikeQuery.existsByUser_UserIdAndBoard(userId, board);
    }
}

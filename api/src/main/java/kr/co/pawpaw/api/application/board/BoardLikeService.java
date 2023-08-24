package kr.co.pawpaw.api.application.board;

import kr.co.pawpaw.common.exception.board.BoardException.BoardNotFoundException;
import kr.co.pawpaw.common.exception.board.BoardLikeException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.board.domain.BoardLikes;
import kr.co.pawpaw.domainrdb.board.service.query.BoardQuery;
import kr.co.pawpaw.domainrdb.board.service.query.BoardLikeQuery;
import kr.co.pawpaw.domainrdb.board.service.command.BoardLikeCommand;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
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
        if (!checkLikeExist(user, board)) {
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
        if (checkLikeExist(user, board)) {
            boardLikeQuery.deleteBoardLikesByUserAndBoard(user, board);
            board.minusLikedCount();
            return true;
        }
        throw new BoardLikeException.BoardDeleteLikeFailException();
    }

    public boolean checkLikeExist(User user, Board board) {
        return boardLikeQuery.existsByUserAndBoard(user, board);
    }
}

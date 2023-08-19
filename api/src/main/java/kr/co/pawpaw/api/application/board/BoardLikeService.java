package kr.co.pawpaw.api.application.board;

import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.board.domain.BoardLikes;
import kr.co.pawpaw.domainrdb.board.repository.BoardLikesRepository;
import kr.co.pawpaw.domainrdb.board.repository.BoardRepository;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final BoardLikesRepository likesRepository;
    private final UserQuery userQuery;
    private final BoardRepository boardRepository;

    @Transactional
    public boolean addOrDeleteLike(Long boardId, UserId userId){
        Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        if (likesNotExist(user, board)){
           likesRepository.save(new BoardLikes(user, board));
           board.plusLikedCount();
            return true;
        }else {
            likesRepository.deleteBoardLikesByUserAndBoard(user, board);
            board.minusLikedCount();
            return false;
        }
    }

    private boolean likesNotExist(User user, Board board){
        return likesRepository.findByUserAndBoard(user, board).isEmpty();  // 좋아요가 없으면 true
    }
}

package kr.co.pawpaw.mysql.boardlike.service.query;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.boardlike.domain.BoardLikes;
import kr.co.pawpaw.mysql.boardlike.repository.BoardLikesRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardLikeQuery {

    private final BoardLikesRepository boardLikesRepository;

    public Optional<BoardLikes> deleteBoardLikesByUserAndBoard(User user, Board board){
       return boardLikesRepository.deleteBoardLikesByUserAndBoard(user, board);
    }
    public boolean existsByUser_UserIdAndBoard(UserId userId, Board board){
        return boardLikesRepository.existsByUser_UserIdAndBoard(userId, board);
    }
}

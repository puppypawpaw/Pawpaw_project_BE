package kr.co.pawpaw.mysql.boardlike.service.query;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.boardlike.domain.BoardLikes;
import kr.co.pawpaw.mysql.boardlike.repository.BoardLikesRepository;
import kr.co.pawpaw.mysql.user.domain.User;
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
    public boolean existsByUserAndBoard(User user, Board board){
        return boardLikesRepository.existsByUserAndBoard(user, board);
    }
}

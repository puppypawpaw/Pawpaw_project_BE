package kr.co.pawpaw.domainrdb.boardlike.service.query;

import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.boardlike.domain.BoardLikes;
import kr.co.pawpaw.domainrdb.boardlike.repository.BoardLikesRepository;
import kr.co.pawpaw.domainrdb.user.domain.User;
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

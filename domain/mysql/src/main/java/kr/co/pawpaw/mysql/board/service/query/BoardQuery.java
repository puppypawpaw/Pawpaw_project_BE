package kr.co.pawpaw.mysql.board.service.query;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.board.repository.BoardCustomRepository;
import kr.co.pawpaw.mysql.board.repository.BoardRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BoardQuery {

    private final BoardRepository boardRepository;
    private final BoardCustomRepository boardCustomRepository;

    public Optional<Board> findById(Long id){
      return boardRepository.findById(id);
    }

    public Slice<Board> getBoardListWithRepliesBy(Pageable pageable){
        return boardCustomRepository.getBoardListWithRepliesBy(pageable);
    }
    public Board getBoardWithRepliesBy(long boardId){
        return boardCustomRepository.getBoardWithRepliesBy(boardId);
    }

    public Slice<Board> getBoardListWithRepliesByUser_UserId(Pageable pageable, UserId userId){
        return boardCustomRepository.getBoardListWithRepliesByUser_UserId(pageable, userId);
    }
    public Slice<Board> getBoardListWithRepliesBySearch(@Param("query") String query, Pageable pageable){
        return boardCustomRepository.getBoardListWithRepliesBySearch(pageable, query);
    }
    public Board findBoardWithFileUrlsById(@Param("id") Long id){
        return boardRepository.findBoardWithFileUrlsById(id);
    }
}

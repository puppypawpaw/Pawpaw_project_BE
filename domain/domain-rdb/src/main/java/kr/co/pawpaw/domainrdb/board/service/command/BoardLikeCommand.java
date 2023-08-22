package kr.co.pawpaw.domainrdb.board.service.command;

import kr.co.pawpaw.domainrdb.board.domain.BoardLikes;
import kr.co.pawpaw.domainrdb.board.repository.BoardLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardLikeCommand {

    private final BoardLikesRepository boardLikesRepository;

    public BoardLikes save(BoardLikes boardLikes){
        return boardLikesRepository.save(boardLikes);
    }
}

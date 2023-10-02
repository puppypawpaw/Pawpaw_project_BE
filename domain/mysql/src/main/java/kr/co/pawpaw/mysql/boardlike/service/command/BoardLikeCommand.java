package kr.co.pawpaw.mysql.boardlike.service.command;

import kr.co.pawpaw.mysql.boardlike.domain.BoardLikes;
import kr.co.pawpaw.mysql.boardlike.repository.BoardLikesRepository;
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

package kr.co.pawpaw.mysql.boardImg.service.command;

import kr.co.pawpaw.mysql.boardImg.domain.BoardImg;
import kr.co.pawpaw.mysql.boardImg.repository.BoardImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardImgCommand {

    private final BoardImgRepository boardImgRepository;

    public BoardImg save(BoardImg boardImg){
        return boardImgRepository.save(boardImg);
    }

    public void delete(BoardImg boardImg){
        boardImgRepository.delete(boardImg);
    }
}

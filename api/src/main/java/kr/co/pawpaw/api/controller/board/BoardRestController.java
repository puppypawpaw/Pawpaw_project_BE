package kr.co.pawpaw.api.controller.board;

import kr.co.pawpaw.api.application.board.BoardService;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.response.BaseResponse;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import kr.co.pawpaw.domainrdb.board.dto.BoardDto.*;

@RestController
@RequiredArgsConstructor
@Getter
@Slf4j
@RequestMapping("/board")
public class BoardRestController {

    private final BoardService boardService;

    @PostMapping("/register")
    public BaseResponse<BoardResponseDto> register(@AuthenticatedUserId UserId userId, @RequestBody BoardRegisterDto registerDto){

        BoardResponseDto boardResponseDto = boardService.register(userId, registerDto);
        return BaseResponse.of("등록이 완료되었습니다.", boardResponseDto);
    }

    @PostMapping("/update")
    public BaseResponse<BoardUpdateDto> updateBoard(@AuthenticatedUserId UserId userId, @RequestParam Long boardId, @RequestBody BoardUpdateDto updateDto){
        BoardUpdateDto boardUpdateDto = boardService.update(userId, boardId, updateDto);
        return BaseResponse.of("정보 수정이 완료되었습니다.", boardUpdateDto);
    }


    @DeleteMapping("/remove")
    public BaseResponse<?> removeBoard(@AuthenticatedUserId UserId userId, @RequestParam Long boardId){
        boardService.removeBoard(userId, boardId);


        return BaseResponse.of("삭제가 완료되었습니다.");
    }


}

package com.puppy.pawpaw_project_be.domain.board.controller;

import com.puppy.pawpaw_project_be.config.annotation.AuthenticatedUserId;
import com.puppy.pawpaw_project_be.domain.board.dto.BoardDto;
import com.puppy.pawpaw_project_be.domain.board.service.BoardService;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.global.response.BaseResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Getter
@Slf4j
@RequestMapping("/board")
public class BoardRestController {

    private final BoardService boardService;

    @PostMapping("/register")
    public BaseResponse<BoardDto.BoardResponseDto> register(@AuthenticatedUserId UserId userId, @RequestBody BoardDto.BoardRegisterDto registerDto){

        BoardDto.BoardResponseDto boardResponseDto = boardService.register(userId, registerDto);
        return BaseResponse.of("등록이 완료되었습니다.", boardResponseDto);
    }

    @PostMapping("/update")
    public BaseResponse<BoardDto.BoardUpdateDto> updateBoard(@AuthenticatedUserId UserId userId, @RequestParam Long boardId, @RequestBody BoardDto.BoardUpdateDto updateDto){
        BoardDto.BoardUpdateDto boardUpdateDto = boardService.update(userId, boardId, updateDto);
        return BaseResponse.of("정보 수정이 완료되었습니다.", boardUpdateDto);
    }


    @DeleteMapping("/remove")
    public BaseResponse<?> removeBoard(@AuthenticatedUserId UserId userId, @RequestParam Long boardId){
        boardService.removeBoard(userId, boardId);


        return BaseResponse.of("삭제가 완료되었습니다.");
    }


}

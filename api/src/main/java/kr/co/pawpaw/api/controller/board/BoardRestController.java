package kr.co.pawpaw.api.controller.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.pawpaw.api.service.board.BoardService;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.board.BoardDto;
import kr.co.pawpaw.api.dto.board.BoardDto.BoardListDto;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Getter
@Slf4j
@RequestMapping("/board")
public class BoardRestController {

    private final BoardService boardService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "게시글 등록에 실패했습니다")
    })
    @Operation(
            method = "POST",
            summary = "게시글 등록",
            description = "게시글을 작성한다."
    )
    @PostMapping("/register")
    public ResponseEntity<BoardDto.RegisterResponseDto> register(@AuthenticatedUserId UserId userId, @RequestBody BoardDto.BoardRegisterDto registerDto){
        BoardDto.RegisterResponseDto boardResponseDto = boardService.register(userId, registerDto);
        return ResponseEntity.ok(boardResponseDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "게시글 수정에 실패했습니다")
    })
    @Operation(
            method = "POST",
            summary = "게시글 수정",
            description = "게시글을 수정한다."
    )
    @PostMapping("/update")
    public ResponseEntity<BoardDto.BoardUpdateDto> updateBoard(@AuthenticatedUserId UserId userId, @RequestParam Long boardId, @RequestBody BoardDto.BoardUpdateDto updateDto){
        BoardDto.BoardUpdateDto boardUpdateDto = boardService.update(userId, boardId, updateDto);
        return ResponseEntity.ok(boardUpdateDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "게시글 삭제에 실패했습니다")
    })
    @Operation(
            method = "PATCH",
            summary = "게시글 삭제",
            description = "게시글을 삭제한다."
    )
    @PatchMapping("/remove")
    public ResponseEntity<Boolean> removeBoard(@AuthenticatedUserId UserId userId, @RequestParam Long boardId){
        return ResponseEntity.ok(boardService.removeBoard(userId, boardId));
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "게시글 리스트 조회에 실패한다.")
    })

    @Operation(
            method = "GET",
            summary = "게시글 리스트 조회",
            description = "게시글 리스트를 가지고 온다."
    )
    @GetMapping("/list")
    public ResponseEntity<Slice<BoardListDto>> getList(@AuthenticatedUserId UserId userId,
                                                    @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                    @RequestParam(value = "pageSize", defaultValue = "2") int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(boardService.getBoardListWithReplies(userId, pageable));
    }


}

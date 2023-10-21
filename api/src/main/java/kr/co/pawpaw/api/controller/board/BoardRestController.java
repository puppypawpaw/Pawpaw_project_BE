package kr.co.pawpaw.api.controller.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.board.BoardDto;
import kr.co.pawpaw.api.dto.board.BoardDto.BoardListDto;
import kr.co.pawpaw.api.service.board.BoardService;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "boards", description = "게시물 API")
@RestController
@RequiredArgsConstructor
@Getter
@Slf4j
@RequestMapping("/api/board")
public class BoardRestController {

    private final BoardService boardService;

    @Operation(
            method = "POST",
            summary = "게시글 등록",
            description = "제목과 내용을 입력받아 게시글을 작성한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 등록 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardDto.RegisterResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "게시글 등록에 실패했습니다", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<BoardDto.RegisterResponseDto> register(
            @AuthenticatedUserId UserId userId,
            @RequestBody BoardDto.BoardRegisterDto registerDto) {
        BoardDto.RegisterResponseDto boardResponseDto = boardService.register(userId, registerDto);
        return ResponseEntity.ok(boardResponseDto);
    }

    @Operation(
            method = "POST",
            summary = "게시글 수정",
            description = "게시글을 수정한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardDto.BoardUpdateDto.class))),
            @ApiResponse(responseCode = "400", description = "게시글 수정에 실패했습니다", content = @Content)
    })
    @PatchMapping("/update/{boardId}")
    public ResponseEntity<BoardDto.BoardUpdateDto> updateBoard(
            @AuthenticatedUserId UserId userId,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId,
            @RequestBody BoardDto.BoardUpdateDto updateDto) {
        BoardDto.BoardUpdateDto boardUpdateDto = boardService.update(userId, boardId, updateDto);
        return ResponseEntity.ok(boardUpdateDto);
    }

    @Operation(
            method = "DELETE",
            summary = "게시글 삭제",
            description = "게시글을 삭제한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "게시글 삭제에 실패했습니다", content = @Content)
    })
    @DeleteMapping("/remove/{boardId}")
    public ResponseEntity<Boolean> removeBoard(
            @AuthenticatedUserId UserId userId,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.removeBoard(userId, boardId));
    }

    @Operation(
            method = "GET",
            summary = "게시글 리스트 조회",
            description = "게시글 리스트를 가지고 온다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 리스트 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardListDto.class))),
            @ApiResponse(responseCode = "400", description = "게시글 리스트 조회에 실패했습니다.", content = @Content)
    })
    @GetMapping("/list")
    public ResponseEntity<Slice<BoardListDto>> getList(
            @AuthenticatedUserId UserId userId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @Parameter(description = "한 페이지에 보여줄 게시글 수") @RequestParam(value = "pageSize", defaultValue = "4") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(boardService.getBoardListWithRepliesBy(userId, pageable));
    }

    @Operation(
            method = "GET",
            summary = "게시글 조회",
            description = "게시글 하나를 가지고 온다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardListDto.class))),
            @ApiResponse(responseCode = "400", description = "게시글 조회에 실패했습니다.", content = @Content)
    })
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardListDto> getBoard(
            @AuthenticatedUserId UserId userId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @Parameter(description = "한 페이지에 보여줄 댓글 수") @RequestParam(value = "pageSize", defaultValue = "4") int pageSize,
            @PathVariable long boardId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(boardService.getBoardWithRepliesBy(boardId, userId, pageable));
    }

    @Operation(
            method = "GET",
            summary = "게시글 검색",
            description = "게시글을 검색한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 검색 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardListDto.class))),
            @ApiResponse(responseCode = "400", description = "게시글 검색에 실패했습니다.", content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<Slice<BoardListDto>> getSearchList(
            @AuthenticatedUserId UserId userId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @Parameter(description = "한 페이지에 보여줄 게시글 수") @RequestParam(value = "pageSize", defaultValue = "4") int pageSize,
            @Parameter(description = "검색어") @RequestParam(value = "query", required = false) String query) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(boardService.searchBoardsByQuery(userId, pageable, query));
    }

    @Operation(
            method = "GET",
            summary = "마이 페이지에서 내가 작성한 글 조회",
            description = "마이 피드를 가지고 온다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "My Feed 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardListDto.class))),
            @ApiResponse(responseCode = "400", description = "My Feed 조회에 실패했습니다.", content = @Content)
    })
    @GetMapping("/myPage")
    public ResponseEntity<Slice<BoardListDto>> getMyFeed(
            @AuthenticatedUserId UserId userId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @Parameter(description = "한 페이지에 보여줄 게시글 수") @RequestParam(value = "pageSize", defaultValue = "4") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(boardService.getBoardListWithRepliesByUser_UserId(userId, pageable));
    }

}

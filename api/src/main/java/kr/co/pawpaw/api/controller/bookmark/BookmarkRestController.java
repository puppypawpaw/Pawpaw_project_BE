package kr.co.pawpaw.api.controller.bookmark;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.board.BoardDto;
import kr.co.pawpaw.api.service.bookmark.BookmarkService;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkRestController {

    private final BookmarkService bookmarkService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "북마크 추가에 실패했습니다")
    })
    @Operation(
            method = "POST",
            summary = "북마크 추가",
            description = "북마크에 추가한다."
    )
    @PostMapping("/add")
    public ResponseEntity<Boolean> addBookmark(@RequestParam Long boardId, @AuthenticatedUserId UserId userId) {
        return ResponseEntity.ok(bookmarkService.addBookmark(boardId, userId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "북마크 취소에 실패했습니다")
    })
    @Operation(
            method = "DELETE",
            summary = "북마크 취소",
            description = "북마크를 취소한다."
    )
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteBookmark(@RequestParam Long boardId, @AuthenticatedUserId UserId userId) {
        return ResponseEntity.ok(bookmarkService.deleteBookmark(boardId, userId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "북마크에 추가된 게시글 조회에 실패했습니다")
    })
    @Operation(
            method = "GET",
            summary = "북마크에 추가된 게시글 조회",
            description = "북마크에 추가된 게시글 조회한다."
    )
    @GetMapping("/list")
    public ResponseEntity<Slice<BoardDto.BoardListDto>> getBookmarkList(
            @AuthenticatedUserId UserId userId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @Parameter(description = "한 페이지에 보여줄 게시글 수") @RequestParam(value = "pageSize", defaultValue = "4") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(bookmarkService.getBoardListWithRepliesByUser_UserId(pageable, userId));
    }
}

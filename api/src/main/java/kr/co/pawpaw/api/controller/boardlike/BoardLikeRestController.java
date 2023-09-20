package kr.co.pawpaw.api.controller.boardlike;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.pawpaw.api.service.boardlike.BoardLikeService;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boardLike")
public class BoardLikeRestController {

    private final BoardLikeService boardLikeService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "좋아요 추가에 실패했습니다")
    })
    @Operation(
            method = "POST",
            summary = "좋아요 추가",
            description = "좋아요를 추가한다."
    )
    @PostMapping("/like")
    public ResponseEntity<Boolean> addLike(@RequestParam Long boardId, @AuthenticatedUserId UserId userId) {
        return ResponseEntity.ok(boardLikeService.addLike(boardId, userId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "좋아요 취소에 실패했습니다")
    })
    @Operation(
            method = "DELETE",
            summary = "좋아요 취소",
            description = "좋아요를 취소한다."
    )
    @DeleteMapping("/deleteLike")
    public ResponseEntity<Boolean> deleteLike(@RequestParam Long boardId, @AuthenticatedUserId UserId userId) {
        return ResponseEntity.ok(boardLikeService.deleteLike(boardId, userId));
    }
}

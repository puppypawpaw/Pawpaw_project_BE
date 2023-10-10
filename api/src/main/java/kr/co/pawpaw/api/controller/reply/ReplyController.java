package kr.co.pawpaw.api.controller.reply;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.reply.ReplyDto;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyListDto;
import kr.co.pawpaw.api.service.reply.ReplyService;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "replies", description = "댓글 API")
@RestController
@RequiredArgsConstructor
@Getter
@Slf4j
@RequestMapping("/api/reply")
public class ReplyController {

    private final ReplyService replyService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 등록 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReplyDto.ReplyRegisterDto.class))),
            @ApiResponse(responseCode = "400", description = "댓글 등록에 실패했습니다", content = @Content)
    })
    @Operation(
            method = "POST",
            summary = "댓글 등록",
            description = "댓글을 작성한다."
    )
    @PostMapping("/register")
    public ResponseEntity<ReplyDto.ReplyResponseDto> register(@RequestBody ReplyDto.ReplyRegisterDto registerDto, @AuthenticatedUserId UserId userId) {
        ReplyDto.ReplyResponseDto responseDto = replyService.register(registerDto, userId);
        return ResponseEntity.ok(responseDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReplyDto.ReplyUpdateDto.class))),
            @ApiResponse(responseCode = "400", description = "댓글 수정에 실패했습니다", content = @Content)
    })
    @Operation(
            method = "Patch",
            summary = "댓글 수정",
            description = "댓글을 수정한다."
    )
    @PatchMapping("/update/{replyId}")
    public ResponseEntity<ReplyDto.ReplyResponseDto> update(
            @RequestBody ReplyDto.ReplyUpdateDto updateDto,
            @AuthenticatedUserId UserId userId,
            @Parameter(description = "댓글 ID", required = true) @PathVariable Long replyId) {
        ReplyDto.ReplyResponseDto responseDto = replyService.update(updateDto, userId, replyId);
        return ResponseEntity.ok(responseDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "댓글 삭제에 실패했습니다", content = @Content)
    })
    @Operation(
            method = "DELETE",
            summary = "댓글 삭제",
            description = "댓글을 삭제한다."
    )
    @DeleteMapping("/remove/{boardId}/{replyId}")
    public ResponseEntity<Void> remove(
            @AuthenticatedUserId UserId userId,
            @Parameter(description = "댓글 ID", required = true) @PathVariable Long replyId,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardId) {
        replyService.remove(userId, replyId, boardId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReplyDto.ReplyListDto.class))),
            @ApiResponse(responseCode = "400", description = "댓글 수정에 실패했습니다", content = @Content)
    })
    @Operation(
            method = "GET",
            summary = "댓글 조회",
            description = "댓글을 조회한다."
    )
    @GetMapping("/list")
    public ResponseEntity<Slice<ReplyListDto>> getList(
            @AuthenticatedUserId UserId userId,
            @Parameter(description = "게시글 ID", required = true) @RequestParam Long boardId,
            @Parameter(description = "페이지 번호") @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @Parameter(description = "한 페이지에 보여줄 게시글 수") @RequestParam(value = "pageSize", defaultValue = "3") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(replyService.findReplyListByBoardId(userId, boardId, pageable));
    }
}

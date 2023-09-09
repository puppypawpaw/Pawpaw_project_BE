package kr.co.pawpaw.api.controller.reply;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.pawpaw.api.application.reply.ReplyService;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.reply.ReplyDto;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyListDto;
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
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "댓글 등록에 실패했습니다")
    })
    @Operation(
            method = "POST",
            summary = "댓글 등록",
            description = "댓글을 작성한다."
    )
    @PostMapping("/register")
    public ResponseEntity<ReplyDto.ReplyResponseDto> register(@RequestBody ReplyDto.ReplyRegisterDto registerDto, @AuthenticatedUserId UserId userId){
        ReplyDto.ReplyResponseDto responseDto = replyService.register(registerDto, userId);
        return ResponseEntity.ok(responseDto);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "댓글 수정에 실패했습니다")
    })
    @Operation(
            method = "Patch",
            summary = "댓글 수정",
            description = "댓글을 수정한다."
    )
    @PatchMapping("/update")
    public ResponseEntity<ReplyDto.ReplyResponseDto> update(@RequestBody ReplyDto.ReplyUpdateDto updateDto, @AuthenticatedUserId UserId userId, @RequestParam Long replyId){
        ReplyDto.ReplyResponseDto responseDto = replyService.update(updateDto, userId, replyId);
        return ResponseEntity.ok(responseDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "댓글 삭제에 실패했습니다")
    })
    @Operation(
            method = "Patch",
            summary = "댓글 삭제",
            description = "댓글을 삭제한다."
    )
    @PatchMapping("/remove")
    public ResponseEntity<Void> remove(@AuthenticatedUserId UserId userId, @RequestParam Long replyId, @RequestParam Long boardId){
        replyService.remove(userId, replyId, boardId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "댓글 조회에 실패했습니다")
    })
    @Operation(
            method = "GET",
            summary = "댓글 조회",
            description = "댓글을 조회한다."
    )
    @GetMapping("/list")
    public ResponseEntity<Slice<ReplyListDto>> getList(@AuthenticatedUserId UserId userId, @RequestParam Long boardId,
                                                    @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                    @RequestParam(value = "pageSize", defaultValue = "3") int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(replyService.findReplyListByBoardId(userId, boardId, pageable));
    }
}

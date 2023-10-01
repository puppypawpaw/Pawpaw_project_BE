package kr.co.pawpaw.api.controller.boardImg;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.boardImg.BoardImgDto;
import kr.co.pawpaw.api.service.boardImg.BoardImgService;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "files", description = "게시글 이미지 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/file")
public class BoardImgRestController {

    private final BoardImgService imgService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 이미지 등록 성공", content = @Content(
                    schema = @Schema(implementation = BoardImgDto.BoardImgUploadDto.class))),
            @ApiResponse(responseCode = "400", description = "이미지 업로드를 실패했습니다", content = @Content)
    })
    @Operation(
            method = "POST",
            summary = "이미지 업로드",
            description = "이미지를 업로드 한다."
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@Parameter(description = "게시글 ID", required = true, example = "1") @RequestParam Long boardId,
                                       @RequestBody BoardImgDto.BoardImgUploadDto uploadDto,
                                       @AuthenticatedUserId UserId userId) {
        imgService.upload(boardId, uploadDto, userId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 이미지 수정 성공", content = @Content(
                    schema = @Schema(implementation = BoardImgDto.BoardImgUploadDto.class))),
            @ApiResponse(responseCode = "400", description = "이미지 수정에 실패했습니다", content = @Content)
    })
    @Operation(
            method = "Patch",
            summary = "이미지 수정",
            description = "이미지를 수정한다."
    )
    @PatchMapping(value = "/update/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> update(@Parameter(description = "게시글 ID", required = true, example = "1") @PathVariable Long boardId,
                                       @RequestBody BoardImgDto.BoardImgUploadDto uploadDto,
                                       @AuthenticatedUserId UserId userId) {
        imgService.update(boardId, uploadDto, userId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "이미지 삭제를 실패했습니다", content = @Content)
    })
    @Operation(
            method = "DELETE",
            summary = "이미지 삭제",
            description = "이미지를 삭제 한다."
    )
    @DeleteMapping("/remove/{boardId}")
    public ResponseEntity<Void> removeImg(@Parameter(description = "게시글 ID", required = true, example = "1") @PathVariable Long boardId) {
        imgService.removeBoardImgFiles(boardId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 조회 성공", content = @Content(
                    schema = @Schema(type = "array", implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "이미지 조회를 실패했습니다", content = @Content)
    })
    @Operation(
            method = "GET",
            summary = "이미지 조회",
            description = "이미지를 조회 한다."
    )
    @GetMapping("/viewImg")
    public ResponseEntity<List<String>> viewImg(@Parameter(description = "게시글 ID", required = true, example = "1") @RequestParam Long boardId) {
        return ResponseEntity.ok(imgService.viewFileImg(boardId));
    }
}

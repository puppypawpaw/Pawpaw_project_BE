package kr.co.pawpaw.api.controller.boardImg;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.pawpaw.api.application.boardImg.BoardImgService;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.boardImg.BoardImgDto;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/file")
public class BoardImgRestController {

    private final BoardImgService imgService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "이미지 업로드를 실패했습니다")
    })
    @Operation(
            method = "POST",
            summary = "이미지 업로드",
            description = "이미지를 업로드 한다."
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@RequestParam Long boardId, @NonNull BoardImgDto.BoardImgUploadDto uploadDto, @AuthenticatedUserId UserId userId){
        imgService.upload(boardId, uploadDto, userId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "이미지 수정에 실패했습니다")
    })
    @Operation(
            method = "Patch",
            summary = "이미지 수정",
            description = "이미지를 수정한다."
    )
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> update(@RequestParam Long boardId, @NonNull BoardImgDto.BoardImgUploadDto uploadDto, @AuthenticatedUserId UserId userId){
        imgService.update(boardId, uploadDto, userId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "이미지 삭제를 실패했습니다")
    })
    @Operation(
            method = "DELETE",
            summary = "이미지 삭제",
            description = "이미지를 삭제 한다."
    )
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeImg(@RequestParam Long boardId){
        imgService.removeBoardImgFiles(boardId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "이미지 조회를 실패했습니다")
    })
    @Operation(
            method = "GET",
            summary = "이미지 조회",
            description = "이미지를 조회 한다."
    )
    @GetMapping("/viewImg")
    public ResponseEntity<List<String>> viewImg(@RequestParam Long boardId) {
        return ResponseEntity.ok(imgService.viewFileImg(boardId));
    }
}

package kr.co.pawpaw.api.controller.boardImg;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.pawpaw.api.application.boardImg.BoardImgService;
import kr.co.pawpaw.api.dto.boardImg.BoardImgDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<BoardImgDto.BoardImgResultDto>> upload(@RequestParam Long boardId, BoardImgDto.BoardImgUploadDto uploadDto){
        log.info("uploadFile = {}", uploadDto);
        List<BoardImgDto.BoardImgResultDto> upload = imgService.upload(boardId, uploadDto);
        return ResponseEntity.ok(upload);
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
    @GetMapping("/viewImg/{fileName}")
    public ResponseEntity<Resource> viewImg(@PathVariable String fileName){
        return imgService.viewFileImg(fileName);
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
    @DeleteMapping("/remove/{fileName}")
    public ResponseEntity<Map<String, Boolean>> removeImg(@PathVariable String fileName){
        Map<String, Boolean> booleanMap = imgService.removeUploadFile(fileName);
        return ResponseEntity.ok(booleanMap);
    }
}

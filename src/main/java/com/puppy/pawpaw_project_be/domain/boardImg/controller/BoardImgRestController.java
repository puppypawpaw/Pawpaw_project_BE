package com.puppy.pawpaw_project_be.domain.boardImg.controller;

import com.puppy.pawpaw_project_be.domain.boardImg.dto.BoardImgDto.BoardImgResultDto;
import com.puppy.pawpaw_project_be.domain.boardImg.dto.BoardImgDto.BoardImgUploadDto;
import com.puppy.pawpaw_project_be.domain.boardImg.service.BoardImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/file")
public class BoardImgRestController {
    private final BoardImgService imgService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<BoardImgResultDto>> upload(BoardImgUploadDto uploadDto){
        log.info("uploadFile = {}", uploadDto);
        List<BoardImgResultDto> upload = imgService.upload(uploadDto);
        return ResponseEntity.ok(upload);
    }

    @GetMapping("/viewImg/{fileName}")
    public ResponseEntity<Resource> viewImg(@PathVariable String fileName){
        return imgService.viewFileImg(fileName);
    }

    @DeleteMapping("/remove/{fileName}")
    public ResponseEntity<Map<String, Boolean>> removeImg( @PathVariable String fileName){
        Map<String, Boolean> booleanMap = imgService.removeUploadFile(fileName);
        return ResponseEntity.ok(booleanMap);
    }
}

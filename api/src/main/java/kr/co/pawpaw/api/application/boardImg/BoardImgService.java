package kr.co.pawpaw.api.application.boardImg;


import kr.co.pawpaw.api.dto.boardImg.BoardImgDto;
import kr.co.pawpaw.common.exception.board.BoardException;
import kr.co.pawpaw.common.exception.board.BoardImgException;
import kr.co.pawpaw.common.exception.board.BoardImgException.BoardImgCanNotRemoveException;
import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.board.service.query.BoardQuery;
import kr.co.pawpaw.domainrdb.boardImg.domain.BoardImg;
import kr.co.pawpaw.domainrdb.boardImg.service.command.BoardImgCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardImgService {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    private final BoardQuery boardQuery;
    private final BoardImgCommand boardImgCommand;

    public List<BoardImgDto.BoardImgResultDto> upload(Long boardId, BoardImgDto.BoardImgUploadDto uploadDto) {
        log.info("uploadFile = {}", uploadDto);

        Board board = boardQuery.findById(boardId)
                .orElseThrow(() -> new BoardException.BoardNotFoundException());

        List<BoardImgDto.BoardImgResultDto> resultList = new ArrayList<>();

        if (uploadDto.getFiles() != null) {
            uploadDto.getFiles().forEach(multipartFile -> {

                boolean image = false;
                String originalFilename = multipartFile.getOriginalFilename();
                log.info("originalFilename = {}", originalFilename);
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalFilename);

                try {
                    multipartFile.transferTo(savePath);

                    BoardImg boardImg = BoardImg.builder()
                            .uuid(uuid)
                            .fileName(originalFilename)
                            .board(board)
                            .build();
                    boardImgCommand.save(boardImg);

                    if (Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
//                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalFilename);
//                                + extractExt(originalFilename));
//                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                    }
                } catch (IOException e) {
                    throw new BoardImgException.BoardImgCanNotUploadException();
                }
                resultList.add(
                        new BoardImgDto.BoardImgResultDto(uuid, originalFilename, image)

                );
            });
            return resultList;
        }
        return null;
    }

    public ResponseEntity<Resource> viewFileImg(String fileName) {
        FileSystemResource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new BoardImgException.BoardImgCanNotViewException();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }


    public Map<String, Boolean> removeUploadFile(String fileName) {
        boolean removed = false;
        HashMap<String, Boolean> resultMap = new HashMap<>();
        FileSystemResource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceFilename = resource.getFilename();

        try {
            removed = resource.getFile().delete();
            String contentType = Files.probeContentType(resource.getFile().toPath());
//            if (contentType.startsWith("image")) {
//                File thumbFile = new File(uploadPath, "s_" + File.separator + "s_" + fileName);
//                thumbFile.delete();
//            }

        } catch (IOException e) {
            throw new BoardImgCanNotRemoveException();
        }
        resultMap.put("result", removed); // 지금은 boolean 값만 반환하지만 나중에 더 다양한 값을 map에 넣어서 반환할 수 있다.
        return resultMap;
    }

    private String extractExt(String originFileName) {
        int index = originFileName.lastIndexOf(".");
        return originFileName.substring(index);
    }

}

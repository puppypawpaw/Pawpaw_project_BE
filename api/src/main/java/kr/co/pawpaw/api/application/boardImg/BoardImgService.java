package kr.co.pawpaw.api.application.boardImg;


import kr.co.pawpaw.api.application.file.FileService;
import kr.co.pawpaw.api.dto.boardImg.BoardImgDto;
import kr.co.pawpaw.common.exception.board.BoardException;
import kr.co.pawpaw.domainrdb.board.domain.Board;
import kr.co.pawpaw.domainrdb.board.service.query.BoardQuery;
import kr.co.pawpaw.domainrdb.boardImg.domain.BoardImg;
import kr.co.pawpaw.domainrdb.boardImg.service.command.BoardImgCommand;
import kr.co.pawpaw.domainrdb.boardImg.service.query.BoardImgQuery;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardImgService {

    private final BoardQuery boardQuery;
    private final BoardImgQuery imgQuery;
    private final BoardImgCommand imgCommand;
    private final FileService fileService;


    @Transactional
    public List<File> upload(Long boardId, BoardImgDto.BoardImgUploadDto uploadDto, UserId userId) {
        Board board = boardQuery.findById(boardId).orElseThrow(BoardException.BoardNotFoundException::new);

        List<BoardImg> boardImgs = uploadDto.getFiles().stream()
                .map(file -> createBoardImg(board, file, userId))
                .collect(Collectors.toList());

        List<File> fileList = boardImgs.stream()
                .map(BoardImg::getFile)
                .collect(Collectors.toList());

        for (BoardImg boardImg : boardImgs) {
            imgCommand.save(boardImg);
        }

        return fileList;
    }

    @Transactional
    public List<File> update(Long boardId, BoardImgDto.BoardImgUploadDto uploadDto, UserId userId) {
        boardQuery.findById(boardId).orElseThrow(BoardException.BoardNotFoundException::new);
        removeBoardImgFiles(boardId);
        return upload(boardId, uploadDto, userId);
    }

    @Transactional
    public void removeBoardImgFiles(Long boardId) {
        List<BoardImg> boardImgList = imgQuery.findBoardImgsWithFileByBoardId(boardId);
        List<String> fileNameList = boardImgList.stream().map(boardImg -> boardImg.getFile().getFileName())
                .collect(Collectors.toList());

        for (String fileName : fileNameList) {
            fileService.deleteFileByName(fileName);
        }
        for (BoardImg boardImg : boardImgList) {
            imgCommand.delete(boardImg);
        }
    }

    public List<String> viewFileImg(Long boardId) {
        List<BoardImg> boardImgList = imgQuery.findBoardImgsWithFileByBoardId(boardId);
        return boardImgList.stream().map(boardImg -> fileService.getUrl(boardImg.getFile().getFileName()))
                .collect(Collectors.toList());
    }

    private BoardImg createBoardImg(Board board, MultipartFile file, UserId userId) {
        File savedFile = fileService.saveFileByMultipartFile(file, userId);
        return BoardImg.builder()
                .board(board)
                .uuid(UUID.randomUUID().toString())
                .file(savedFile)
                .build();
    }

}

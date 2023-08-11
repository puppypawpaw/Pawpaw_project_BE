package com.puppy.pawpaw_project_be.domain.board.controller;

import com.puppy.pawpaw_project_be.config.annotation.AuthenticatedUserId;
import com.puppy.pawpaw_project_be.domain.board.service.BoardLikeService;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boardLike")
public class BoardLikeRestController {

    private final BoardLikeService boardLikeService;

    @PostMapping("/like")
    public ResponseEntity addOrDeleteLike(@RequestParam Long boardId, @AuthenticatedUserId UserId userId){
        boolean result = boardLikeService.addOrDeleteLike(boardId, userId);
        return ResponseEntity.ok(result);
    }
}

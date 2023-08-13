package com.puppy.pawpaw_project_be.domain.board.entity;

import com.puppy.pawpaw_project_be.domain.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardTest {
    @Autowired
    BoardRepository boardRepository;

    @Test
    @DisplayName("insertWithImages")
    void insertWithImages() throws Exception {
        //given
        Board board = Board.builder()
                .title("test")
                .content("test")
                .writer("test")
                .build();
        //when
        for (int i = 0; i < 3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
        }

        boardRepository.save(board);
        //then
    }

}
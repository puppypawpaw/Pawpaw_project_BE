package com.puppy.pawpaw_project_be.domain.board.repository;

import com.puppy.pawpaw_project_be.domain.board.dto.BoardDto;
import com.puppy.pawpaw_project_be.domain.board.entity.Board;
import com.puppy.pawpaw_project_be.domain.comment.repository.CommentRepository;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    private User createUser() {
        User user = User.builder()
                .id(UserId.create().getValue())
                .nickname("nickname")
                .password("password")
                .phoneNumber("01011112222")
                .imageUrl(null)
                .build();
        return userRepository.save(user);
    }

    private Board createBoard(User user) {
        BoardDto.BoardRegisterDto boardRegisterDto = new BoardDto.BoardRegisterDto("title", "content");
        Board board = Board.createBoard(boardRegisterDto, user);
        return boardRepository.save(board);
    }
    @Test
    @DisplayName("첨부파일이 있는 게시글을 등록한다")
    void insertWithImages() throws Exception {
        //given
        User user = createUser();

        BoardDto.BoardRegisterDto boardRegisterDto = new BoardDto.BoardRegisterDto("title", "content");
        Board board = Board.createBoard(boardRegisterDto, user);
        //when
        for (int i = 0; i < 3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }
        Board savedBoard = boardRepository.save(board);  // cascade all 이므로 게시글이 저장될 때, 이미지도 모두 저장된다.
        //then
        assertThat(savedBoard.getImgSet()).hasSize(3);
    }

    @Test
    @DisplayName("첨부파일이 있는 게시글을 조회한다")
    @Transactional
    void ReadWithImages() throws Exception {
        //given
        User user = createUser();
        Board board = createBoard(user);

        //when
        for (int i = 0; i < 3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }
        boardRepository.save(board);

        Board result = boardRepository.findByIdWithImages(9L).orElseThrow();
        //then
        assertThat(result.getImgSet())
                .hasSize(3)
                .extracting("fileName", "ord")
                .containsExactlyInAnyOrder(
                        tuple("file0.jpg", 0),
                        tuple("file1.jpg", 1),
                        tuple("file2.jpg", 2)
                );
    }

    @Test
    @DisplayName("등록된 첨부파일을 수정한다. 수정은 등록된 파일을 삭제하고 다시 등록하는 형식으로 이루어진다")
    @Commit // 롤백하지 않고 결과를 확인가능하다
    void testModifyImages() throws Exception {
        //given
        User user = createUser();
        Board board = createBoard(user);
        //when
        board.clearImages();

        for (int i = 0; i < 3; i++) {
            board.addImage(UUID.randomUUID().toString(), "update" + i + ".jpg");
        }
        boardRepository.save(board);
        //then
        assertThat(board.getImgSet())
                .hasSize(3)
                .extracting("fileName", "ord")
                .containsExactlyInAnyOrder(
                        tuple("update0.jpg", 0),
                        tuple("update1.jpg", 1),
                        tuple("update2.jpg", 2)
                );
    }
}
package kr.co.pawpaw.mysql.board.service.command;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.board.repository.BoardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class BoardCommandTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardCommand boardCommand;

    @Test
    @DisplayName("save")
    void save() throws Exception {
        //given
        Board board = Board.builder()
                .content("test")
                .build();
        when(boardRepository.save(eq(board))).thenReturn(board);
        //when
        Board savedBoard = boardCommand.save(board);
        //then
        verify(boardRepository).save(board);
        assertThat(savedBoard.getContent()).isEqualTo(board.getContent());
    }

}
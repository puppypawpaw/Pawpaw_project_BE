package kr.co.pawpaw.mysql.board.service.query;


import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.board.repository.BoardCustomRepository;
import kr.co.pawpaw.mysql.board.repository.BoardRepository;
import kr.co.pawpaw.mysql.reply.domain.Reply;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardQueryTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private BoardCustomRepository boardCustomRepository;
    @InjectMocks
    private BoardQuery boardQuery;

    private static User user = User.builder()
            .nickname("nickname")
            .password("password")
            .phoneNumber("01011112222")
            .nickname("nickname")
            .build();

    private static Board board = Board.builder()
            .user(user)
            .writer(user.getNickname())
            .content("content")
            .fileUrls(List.of("file_url", "file_url2"))
            .build();

    private static Reply reply = Reply.builder()
            .parent(null)
            .user(user)
            .board(board)
            .content("replyContent")
            .build();


    @Test
    @DisplayName("findById")
    void findById() throws Exception {
        //when
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        Optional<Board> result = boardQuery.findById(1L);

        //then
        verify(boardRepository).findById(1L);
        assertTrue(result.isPresent());
        assertSame(board, result.orElseThrow());
    }

    @Test
    @DisplayName("getBoardListWithRepliesBy")
    void getBoardListWithRepliesBy() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        List<Board> boardList = Arrays.asList(board, board);
        when(boardCustomRepository.getBoardListWithRepliesBy(pageable)).thenReturn(new SliceImpl<>(boardList, pageable, false));

        //when
        Slice<Board> result = boardQuery.getBoardListWithRepliesBy(pageable);

        //then
        verify(boardCustomRepository, times(1)).getBoardListWithRepliesBy(pageable);
        assertThat(result.getSize()).isEqualTo(2);

    }

    @Test
    @DisplayName("getBoardWithRepliesBy")
    void getBoardWithRepliesBy() throws Exception {
        //given
        when(boardCustomRepository.getBoardWithRepliesBy(1L)).thenReturn(board);
        //when
        Board result = boardQuery.getBoardWithRepliesBy(1L);
        result.getReply().add(reply);
        //then
        verify(boardCustomRepository, times(1)).getBoardWithRepliesBy(1L);
        assertNotNull(result);
        assertThat(board.getId()).isEqualTo(result.getId());
        assertThat(board.getReply().size()).isEqualTo(result.getReply().size());
    }

    @Test
    @DisplayName("getBoardListWithRepliesByUser_UserId")
    void getBoardListWithRepliesByUserUserId() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        List<Board> boardList = List.of(board, board);
        UserId userId = UserId.create();

        when(boardCustomRepository.getBoardListWithRepliesByUser_UserId(pageable, userId)).thenReturn(new SliceImpl<>(boardList, pageable, false));
        //when
        Slice<Board> result = boardQuery.getBoardListWithRepliesByUser_UserId(pageable, userId);

        //then
        verify(boardCustomRepository, times(1)).getBoardListWithRepliesByUser_UserId(pageable, userId);
        assertThat(result.getSize()).isEqualTo(2);

    }

    @Test
    @DisplayName("findBoardWithFileUrlsById")
    void findBoardWithFileUrlsById() throws Exception {
        //given
        when(boardRepository.findBoardWithFileUrlsById(1L)).thenReturn(board);
        //when
        Board result = boardQuery.findBoardWithFileUrlsById(1L);
        //then
        verify(boardRepository, times(1)).findBoardWithFileUrlsById(1L);
        Assertions.assertThat(result.getFileUrls()).hasSize(2)
                .containsExactlyInAnyOrder("file_url", "file_url2");
    }

}
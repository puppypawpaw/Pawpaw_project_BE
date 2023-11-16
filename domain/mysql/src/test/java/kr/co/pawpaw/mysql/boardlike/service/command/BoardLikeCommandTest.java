package kr.co.pawpaw.mysql.boardlike.service.command;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.boardlike.domain.BoardLikes;
import kr.co.pawpaw.mysql.boardlike.repository.BoardLikesRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BoardLikeCommandTest {

    @Mock
    private BoardLikesRepository boardLikesRepository;

    @InjectMocks
    private BoardLikeCommand boardLikeCommand;

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

    private static BoardLikes boardLikes = new BoardLikes(user, board);

    @Test
    @DisplayName("save")
    void save() throws Exception {
        //given
        when(boardLikesRepository.save(boardLikes)).thenReturn(boardLikes);
        //when
        BoardLikes result = boardLikeCommand.save(boardLikes);
        //then
        verify(boardLikesRepository).save(boardLikes);
        assertAll("save",
                () -> assertThat(result.getBoard().getId()).isEqualTo(boardLikes.getBoard().getId()),
                () -> assertThat(result.getBoard().getContent()).isEqualTo(boardLikes.getBoard().getContent()),
                () -> assertThat(result.getBoard().getWriter()).isEqualTo(boardLikes.getBoard().getWriter())
        );

    }

}
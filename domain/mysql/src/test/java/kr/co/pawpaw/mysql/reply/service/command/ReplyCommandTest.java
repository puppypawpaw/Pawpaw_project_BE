package kr.co.pawpaw.mysql.reply.service.command;


import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.reply.domain.Reply;
import kr.co.pawpaw.mysql.reply.repository.ReplyRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReplyCommandTest {

    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ReplyCommand replyCommand;


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
            .build();

    private static Reply parentReply = Reply.builder()
            .parent(null)
            .writer(user.getNickname())
            .content("testContent")
            .board(board)
            .build();

    private static Reply childReply = Reply.builder()
            .parent(parentReply)
            .writer(user.getNickname())
            .content("testContent")
            .board(board)
            .build();

    @Nested
    @DisplayName("댓글을 저장한다")
    class saveReply {
        @Test
        @DisplayName("자식 댓글이 존재하지 않는 댓글을 저장한다.")
        void saveReplyWithoutParent() {
            // Given
            when(replyRepository.save(parentReply)).thenReturn(parentReply);

            // When
            Reply parentResult = replyCommand.save(parentReply);


            // Then
            verify(replyRepository, times(1)).save(parentResult);

            assertAll("Saved Reply Without Parent",
                    () -> assertThat(parentReply.getWriter()).isEqualTo(parentResult.getWriter()),
                    () -> assertThat(parentReply.getContent()).isEqualTo(parentResult.getContent()),
                    () -> assertThat(parentReply.getBoard().getId()).isEqualTo(parentResult.getBoard().getId()),
                    () -> assertThat(parentReply.getParent()).isNull()
            );
        }

        @Test
        @DisplayName("Save Reply With Parent")
        void saveReplyWithParent() {
            // Given
            when(replyRepository.save(childReply)).thenReturn(childReply);

            // When
            Reply childResult = replyCommand.save(childReply);

            // Then
            verify(replyRepository, times(1)).save(childReply);
            assertAll("Saved Reply With Parent",
                    () -> assertThat(childReply.getWriter()).isEqualTo(childResult.getWriter()),
                    () -> assertThat(childReply.getContent()).isEqualTo(childResult.getContent()),
                    () -> assertThat(childReply.getBoard().getId()).isEqualTo(childResult.getBoard().getId()),
                    () -> assertThat(childReply.getParent().getId()).isEqualTo(childResult.getParent().getId()),
                    () -> assertThat(childResult.getParent()).isNotNull()
            );
        }
    }

}
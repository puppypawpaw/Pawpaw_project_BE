package kr.co.pawpaw.mysql.reply.service.query;



import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.reply.domain.Reply;
import kr.co.pawpaw.mysql.reply.repository.ReplyCustomRepository;
import kr.co.pawpaw.mysql.reply.repository.ReplyRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReplyQueryTest {

    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private ReplyCustomRepository replyCustomRepository;

    @InjectMocks
    private ReplyQuery replyQuery;

    private User user = User.builder()
            .nickname("nickname")
            .password("password")
            .phoneNumber("01011112222")
            .build();
    private Board board = Board.builder()
            .user(user)
            .writer(user.getNickname())
            .content("content")
            .build();
    private Reply reply = Reply.builder()
            .parent(null)
            .user(user)
            .writer(user.getNickname())
            .content("testContent")
            .board(board)
            .build();
    private Reply childReply = Reply.builder()
            .parent(reply)
            .user(user)
            .writer(user.getNickname())
            .content("childTestContent")
            .board(board)
            .build();

    @Nested
    @DisplayName("댓글을 찾는다")
    class findReply {
        @Test
        @DisplayName("findReplyById")
        void findReplyById() throws Exception {
            //given
            when(replyRepository.findReplyById(1L)).thenReturn(Optional.of(reply));

            //when
            Optional<Reply> result = replyQuery.findReplyById(1L);

            //then
            verify(replyRepository).findReplyById(1L);

            assertAll("findReplyById",
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.isPresent()).isTrue(),
                    () -> assertThat(result.get().getContent()).isEqualTo("testContent"),
                    () -> assertThat(result.get().getWriter()).isEqualTo("nickname")
            );
        }

        @Test
        @DisplayName("findReplyByIdWithParentAndUser_UserId")
        void findReplyByIdWithParentAndUser_UserId() throws Exception {
            //given
            UserId userId = UserId.create();
            when(replyRepository.findReplyByIdWithParentAndUser_UserId(1L, userId)).thenReturn(Optional.of(childReply));
            //when
            Optional<Reply> result = replyQuery.findReplyByIdWithParentAndUser_UserId(1L, userId);

            //then
            verify(replyRepository).findReplyByIdWithParentAndUser_UserId(1L, userId);
            assertAll("findReplyByIdWithParentAndUser_UserId",
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.get().getParent()).isNotNull(),
                    () -> assertThat(result.get().getUser().getNickname()).isEqualTo("nickname"),
                    () -> assertThat(result.get().getParent().getContent()).isEqualTo("testContent"),
                    () -> assertThat(result.get().getContent()).isEqualTo("childTestContent")
            );

        }

        @Test
        @DisplayName("findRepliesWithChildren")
        void findRepliesWithChildren() throws Exception {
            //given
            when(replyRepository.findRepliesWithChildren(reply)).thenReturn(List.of(reply, childReply));

            //when
            List<Reply> result = replyQuery.findRepliesWithChildren(reply);

            //then
            verify(replyRepository).findRepliesWithChildren(reply);
            assertAll("findRepliesWithChildren",
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result).hasSize(2)
                            .extracting("writer", "content")
                            .containsExactlyInAnyOrder(
                                    tuple("nickname", "testContent"),  // 부모 댓글
                                    tuple("nickname", "childTestContent")  // 자식 댓글
                            )
            );
        }

        @Test
        @DisplayName("removeReplyById")
        void removeReplyById() throws Exception {
            //when
            doNothing().when(replyRepository).removeReplyById(1L);
            assertDoesNotThrow(() -> replyQuery.removeReplyById(1L));

            //then
            verify(replyRepository).removeReplyById(1L);
        }

        @Test
        @DisplayName("checkReplyWriter")
        void checkReplyWriter() throws Exception {
            //given
            when(replyRepository.existsByUserAndBoard(user, board)).thenReturn(true);
            //when
            boolean result = replyQuery.checkReplyWriter(user, board);
            //then
            verify(replyRepository).existsByUserAndBoard(user, board);
            assertTrue(result);
        }

        @Test
        @DisplayName("checkReplyWriter differentUser")
        void checkReplyWriter_differentUser() {
            // given
            User differentUser = User.builder()
                    .nickname("differentNickname")
                    .password("differentPassword")
                    .phoneNumber("01022223333")
                    .build();

            when(replyRepository.existsByUserAndBoard(differentUser, board)).thenReturn(false);

            // when
            boolean result = replyQuery.checkReplyWriter(differentUser, board);

            // then
            verify(replyRepository).existsByUserAndBoard(differentUser, board);
            assertFalse(result);
        }

        @Test
        @DisplayName("findReplyListByBoardId")
        void findReplyListByBoardId() throws Exception {
            //given
            Pageable pageable = PageRequest.of(0, 2);
            when(replyCustomRepository.findReplyListByBoardId(1L, pageable)).thenReturn(new SliceImpl<>(List.of(reply, reply), pageable, false));
            //when
            Slice<Reply> result = replyQuery.findReplyListByBoardId(1L, pageable);
            //then
            verify(replyCustomRepository).findReplyListByBoardId(1L, pageable);
            assertNotNull(result);
            assertThat(result).hasSize(2)
                    .extracting("writer", "content")
                    .containsExactlyInAnyOrder(
                            tuple("nickname", "testContent"),
                            tuple("nickname", "testContent")
                    );

        }
    }
}
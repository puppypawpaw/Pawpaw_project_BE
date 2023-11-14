package kr.co.pawpaw.mysql.boardlike.service.query;

import kr.co.pawpaw.mysql.board.domain.Board;
import kr.co.pawpaw.mysql.boardlike.domain.BoardLikes;
import kr.co.pawpaw.mysql.boardlike.repository.BoardLikesRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardLikeQueryTest {

    @Mock
    private BoardLikesRepository boardLikesRepository;

    @InjectMocks
    private BoardLikeQuery boardLikeQuery;


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

    @Nested
    @DisplayName("게시글에 등록된 boardLike를 반환하거나 하지 않는다")
    class deleteBoardLikesByUserAndBoard {
        @Test
        @DisplayName("deleteBoardLikesByUserAndBoard_whenBoardLikes_exist")
        void deleteBoardLikesByUserAndBoard_whenBoardLikes_exist() throws Exception {
            //given
            when(boardLikesRepository.deleteBoardLikesByUserAndBoard(user, board)).thenReturn(Optional.of(boardLikes));
            //when
            Optional<BoardLikes> result = boardLikeQuery.deleteBoardLikesByUserAndBoard(user, board);
            //then
            verify(boardLikesRepository).deleteBoardLikesByUserAndBoard(user,board);
            assertTrue(result.isPresent());
        }

        @Test
        @DisplayName("deleteBoardLikesByUserAndBoard_whenBoardLikes_NotExist")
        void deleteBoardLikesByUserAndBoard_whenBoardLikes_NotExist() throws Exception {
            //given
            when(boardLikesRepository.deleteBoardLikesByUserAndBoard(user, board)).thenReturn(Optional.empty());
            //when
            Optional<BoardLikes> result = boardLikeQuery.deleteBoardLikesByUserAndBoard(user, board);
            //then
            verify(boardLikesRepository).deleteBoardLikesByUserAndBoard(user,board);
            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("게시글에 좋아요 여부 확인")
    class existsByUserAndBoard {
        @Test
        @DisplayName("existsByUserAndBoard_true")
        void existsByUserAndBoard_true() throws Exception {
            //given
            UserId userId = UserId.create();
            when(boardLikesRepository.existsByUser_UserIdAndBoard(userId, board)).thenReturn(true);

            //when
            boolean result = boardLikeQuery.existsByUser_UserIdAndBoard(userId, board);

            //then
            verify(boardLikesRepository).existsByUser_UserIdAndBoard(userId, board);
            assertTrue(result);
        }

        @Test
        @DisplayName("existsByUserAndBoard_false")
        void existsByUserAndBoard_false() throws Exception {
            //given
            UserId userId = UserId.create();
            when(boardLikesRepository.existsByUser_UserIdAndBoard(userId, board)).thenReturn(false);

            //when
            boolean result = boardLikeQuery.existsByUser_UserIdAndBoard(userId, board);

            //then
            verify(boardLikesRepository).existsByUser_UserIdAndBoard(userId, board);
            assertFalse(result);
        }
    }

}
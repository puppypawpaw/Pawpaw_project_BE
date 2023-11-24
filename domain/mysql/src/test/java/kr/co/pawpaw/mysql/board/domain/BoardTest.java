package kr.co.pawpaw.mysql.board.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
class BoardTest {

    private Board board;

    @BeforeEach
    void setup() {
        board = Board.builder()
                .content("Content")
                .writer("Writer")
                .fileUrls(Arrays.asList("file_url1", "file_url2"))
                .build();
    }

    @Nested
    @DisplayName("좋아요 숫자가 증가 또는 감소한다.")
    class LikedCount {
        @Test
        @DisplayName("plusLikedCount_success")
        void plusLikedCountSuccess() throws Exception {
            //given
            int initialLikedCount = board.getLikedCount();
            //when
            board.plusLikedCount();
            //then
            assertThat(initialLikedCount + 1).isEqualTo(board.getLikedCount());
        }

        @Test
        @DisplayName("minusLikedCount_success")
        void minusLikedCountSuccess() throws Exception {
            //given
            board.plusLikedCount();
            int initialLikedCount = board.getLikedCount();
            //when
            board.minusLikedCount();
            //then
            assertThat(initialLikedCount - 1).isEqualTo(board.getLikedCount());
        }
    }

    @Nested
    @DisplayName("댓글의 개수가 증가 또는 감소한다.")
    class ReplyCount {
        @Test
        @DisplayName("plusReplyCount")
        void plusReplyCount_success() throws Exception {
            //given
            int initialReplyCount = board.getReplyCount();
            //when
            board.plusReplyCount();
            //then
            assertThat(initialReplyCount + 1).isEqualTo(board.getReplyCount());
        }

        @Test
        @DisplayName("minusReplyCount")
        void minusReplyCount_success() throws Exception {
            //given
            board.plusReplyCount();
            int initialReplyCount = board.getReplyCount();
            //when
            board.minusReplyCount();
            //then
            assertThat(initialReplyCount - 1).isEqualTo(board.getReplyCount());
        }
    }

    @Nested
    @DisplayName("게시글의 누적신고횟수가 증가 또는 감소한다.")
    class ReportedCount {
        @Test
        @DisplayName("plusReportedCount")
        void plusReportedCount_success() throws Exception {
            //given
            int initialReportedCount = board.getReportedCount();
            //when
            board.plusReportedCount();
            //then
            assertThat(initialReportedCount + 1).isEqualTo(board.getReportedCount());
        }

        @Test
        @DisplayName("minusReportedCount")
        void minusReportedCount_success() throws Exception {
            //given
            board.plusReportedCount();
            int initialReportedCount = board.getReportedCount();
            //when
            board.minusReportedCount();
            //then
            assertThat(initialReportedCount - 1).isEqualTo(board.getReplyCount());
        }
    }

    @Nested
    @DisplayName("게시글을 북마크에 등록 또는 취소한다.")
    class AddOrDeleteBookmark {
        @Test
        @DisplayName("addBookmark")
        void addBookmark() throws Exception {
            //when
            board.addBookmark();
            //then
            assertTrue(board.isBookmarked());
        }

        @Test
        @DisplayName("deleteBookmark")
        void deleteBookmark() throws Exception {
            //given
            board.addBookmark();
            //when
            board.deleteBookmark();
            //then
            assertFalse(board.isBookmarked());
        }
    }

    @Test
    @DisplayName("게시글 내용을 수정한다.")
    void updateContent() throws Exception {
        //when
        board.updateContent("aa");
        //then
        assertThat(board.getContent()).isEqualTo("aa");
    }

    @Test
    @DisplayName("게시글에 첨부된 파일을 수정한다")
    void updateFileUrl() throws Exception {
        // given
        List<String> newFileUrls = Arrays.asList("file_url10", "file_url20");

        //when
        board.updateFileUrl(newFileUrls);
        //then
        assertEquals(board.getFileUrls().size(), 2);
        assertThat(board.getFileUrls()).hasSize(2)
                .containsExactlyInAnyOrder("file_url10", "file_url20");
    }

    @Test
    @DisplayName("등록된 게시글을 삭제한다")
    void remove_board() throws Exception {
        //when
        board.remove();
        //then
        assertThat(board.isRemoved()).isTrue();
    }
}
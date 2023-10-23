package kr.co.pawpaw.api.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyListDto;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


public class BoardDto {
    @Schema(description = "게시글 등록 DTO")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BoardRegisterDto {
        @Schema(description = "게시글 내용", example = "나이는 1살 입니다.")
        private String content;
        @Builder
        public BoardRegisterDto(String content) {
            this.content = content;
        }
    }
    @Schema(description = "게시글 응답 DTO")
    @Getter
    public static class BoardResponseDto {
        @Schema(description = "게시글 id", example = "14")
        private Long id;
        @Schema(description = "게시글 내용", example = "나이는 1살 입니다.")
        private String content;
        @Schema(description = "게시글 작성자", example = "냥이최고")
        private String writer;
        @Schema(description = "게시글 작성일자", example = "2023-10-16 15:01:36.464359")
        private LocalDateTime createDate;
        @Schema(description = "게시글 수정일자", example = "2023-10-17 15:01:36.464359")
        private LocalDateTime modifiedDate;
        @Schema(description = "게시글 첨부파일", example = "https://pub-2bd8949dbff24e3fbbc30ac222835ad6.r2.dev/8e942645-4276-40a5-b5de-942d0e16b2c7")
        private List<String> fileUrls;
        @Builder
        public BoardResponseDto(Long id, String content, String writer, LocalDateTime createDate, LocalDateTime modifiedDate, List<String> fileUrls) {
            this.id = id;
            this.content = content;
            this.writer = writer;
            this.createDate = createDate;
            this.modifiedDate = modifiedDate;
            this.fileUrls = fileUrls;
        }

    }
    @Schema(description = "게시글 수정 DTO")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BoardUpdateDto {
        @Schema(description = "게시글 내용", example = "나이는 2살 입니다")
        private String content;
        @Builder
        public BoardUpdateDto(String content) {
            this.content = content;
        }
    }
    @Schema(description = "게시글 리스트 DTO")
    @Getter
    public static class BoardListDto {
        @Schema(description = "유저 id", example = "4b067a59-1d81-4d3b-a58f-0843ac1eaa84")
        private UserId userId;
        @Schema(description = "게시글 id", example = "14")
        private Long id;
        @Schema(description = "게시글 내용", example = "나이는 1살 입니다.")
        private String content;
        @Schema(description = "게시글 작성자", example = "냥이최고")
        private String writer;
        @Schema(description = "게시글의 댓글목록")
        private List<ReplyListDto> replyListDto;
        @Schema(description = "게시글의 첨부파일 링크", example = "https://pub-2bd8949dbff24e3fbbc30ac222835ad6.r2.dev/8d41ad2c-cfdd-47bb-aada-210bcffbb559")
        private List<String> fileUrls;
        @Schema(description = "게시글 좋아요 수", example = "10")
        private int likedCount;
        @Schema(description = "게시글 댓글의 수", example = "12")
        private int replyCount;
        @Schema(description = "유저 프로필 url", example = "https://pub-8a7cf54e8cb44eabac95682c0d8fcc42.r2.dev/유저 기본 이미지.jpg")
        private String userImageUrl;
        @Schema(description = "좋아요 여부", example = "true")
        private boolean boardLiked;
        @Schema(description = "북마크 여부", example = "false")
        private boolean bookmarked;
        @Schema(description = "게시글 생성일자", example = "2023-10-17 15:01:36.464359")
        private LocalDateTime createdDate;
        @Schema(description = "게시글 수정일자", example = "2023-10-17 15:01:36.464359")
        private LocalDateTime modifiedDate;

        @Builder
        public BoardListDto(UserId userId, Long id, String content, String writer, int likedCount,
                            int replyCount, String userImageUrl, boolean boardLiked, boolean bookmarked, LocalDateTime createdDate, LocalDateTime modifiedDate,
                            List<ReplyListDto> replyListDto, List<String> fileUrls) {
            this.userId = userId;
            this.id = id;
            this.content = content;
            this.writer = writer;
            this.likedCount = likedCount;
            this.replyCount = replyCount;
            this.userImageUrl = userImageUrl;
            this.boardLiked = boardLiked;
            this.bookmarked = bookmarked;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
            this.replyListDto = replyListDto;
            this.fileUrls = fileUrls;
        }

        public void setReplyListToBoard(List<ReplyListDto> replyListDto){
            this.replyListDto = replyListDto;
        }
        public void setFileUrlsToBoard(List<String> fileUrls){
            this.fileUrls = fileUrls;
        }
    }

}

package kr.co.pawpaw.api.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyListDto;
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
        @Schema(description = "게시글 제목", example = "우리 댕댕이 귀엽죠?")
        private String title;
        @Schema(description = "게시글 내용", example = "나이는 1살 입니다.")
        private String content;
        @Builder
        public BoardRegisterDto(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
    @Schema(description = "게시글 응답 DTO")
    @Getter
    public static class RegisterResponseDto {
        @Schema(description = "게시글 제목")
        private String title;
        @Schema(description = "게시글 내용")
        private String content;
        @Schema(description = "게시글 작성자")
        private String writer;
        @Schema(description = "게시글 작성일자")
        private LocalDateTime createDate;
        @Schema(description = "게시글 수정일자")
        private LocalDateTime modifiedDate;
        @Builder
        public RegisterResponseDto(String title, String content, String writer, LocalDateTime createDate, LocalDateTime modifiedDate) {
            this.title = title;
            this.content = content;
            this.writer = writer;
            this.createDate = createDate;
            this.modifiedDate = modifiedDate;
        }

    }
    @Schema(description = "게시글 수정 DTO")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BoardUpdateDto {
        @Schema(description = "게시글 제목", example = "우리 냥이 귀엽죠?")
        private String title;
        @Schema(description = "게시글 내용", example = "나이는 2살 입니다")
        private String content;
        @Builder
        public BoardUpdateDto(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
    @Schema(description = "게시글 리스트 DTO")
    @Getter
    public static class BoardListDto {
        @Schema(description = "게시글 id")
        private Long id;
        @Schema(description = "게시글 제목")
        private String title;
        @Schema(description = "게시글 내용")
        private String content;
        @Schema(description = "게시글 작성자")
        private String writer;
        @Schema(description = "게시글의 댓글목록")
        private List<ReplyListDto> replyListDto;
        @Schema(description = "게시글의 첨부파일 링크")
        private List<String> fileNames;
        @Schema(description = "게시글 좋아요 수")
        private int likedCount;
        @Schema(description = "게시글 댓글의 수")
        private int replyCount;
        @Schema(description = "유저 프로필 url")
        private String userImageUrl;
        @Schema(description = "게시글 생성일자")
        private LocalDateTime createdDate;
        @Schema(description = "게시글 수정일자")
        private LocalDateTime modifiedDate;

        @Builder
        public BoardListDto(Long id, String title, String content, String writer, int likedCount,
                            int replyCount,  String userImageUrl, LocalDateTime createdDate, LocalDateTime modifiedDate,
                            List<ReplyListDto> replyListDto, List<String> fileNames) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.writer = writer;
            this.likedCount = likedCount;
            this.replyCount = replyCount;
            this.userImageUrl = userImageUrl;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
            this.replyListDto = replyListDto;
            this.fileNames = fileNames;
        }

        public void setReplyListToBoard(List<ReplyListDto> replyListDto){
            this.replyListDto = replyListDto;
        }
    }

}

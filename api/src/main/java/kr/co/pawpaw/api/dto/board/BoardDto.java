package kr.co.pawpaw.api.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyListDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


public class BoardDto {
    @Schema(description = "게시글 등록 DTO")
    @Getter
    public static class BoardRegisterDto {
        @Schema(name = "게시글 제목", example = "우리 댕댕이 귀엽죠?")
        private String title;
        @Schema(name = "게시글 내용", example = "나이는 1살 입니다.")
        private String content;
        @Builder
        public BoardRegisterDto(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
    @Schema(name = "게시글 응답 DTO")
    @Getter
    public static class RegisterResponseDto {
        @Schema(name = "게시글 제목")
        private String title;
        @Schema(name = "게시글 내용")
        private String content;
        @Schema(name = "게시글 작성자")
        private String writer;
        @Schema(name = "게시글 작성일자")
        private LocalDateTime createDate;
        @Schema(name = "게시글 수정일자")
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
    @Schema(name = "게시글 수정 DTO")
    @Getter
    public static class BoardUpdateDto {
        @Schema(name = "게시글 제목", example = "우리 냥이 귀엽죠?")
        private String title;
        @Schema(name = "게시글 내용", example = "나이는 2살 입니다")
        private String content;
        @Builder
        public BoardUpdateDto(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
    @Schema(name = "게시글 리스트 DTO")
    @Getter
    public static class BoardListDto {
        @Schema(name = "게시글 id")
        private Long id;
        @Schema(name = "게시글 제목")
        private String title;
        @Schema(name = "게시글 내용")
        private String content;
        @Schema(name = "게시글 작성자")
        private String writer;
        @Schema(name = "게시글의 댓글목록")
        private List<ReplyListDto> replyListDto;
        @Schema(name = "게시글의 첨부파일 링크")
        private List<String> fileNames;
        @Schema(name = "게시글 좋아요 수")
        private int likedCount;
        @Schema(name = "게시글 댓글의 수")
        private int replyCount;
        @Schema(name = "게시글 생성일자")
        private LocalDateTime createdDate;
        @Schema(name = "게시글 수정일자")
        private LocalDateTime modifiedDate;

        @Builder
        public BoardListDto(Long id, String title, String content, String writer, int likedCount,
                            int replyCount, LocalDateTime createdDate, LocalDateTime modifiedDate,
                            List<ReplyListDto> replyListDto,  List<String> fileNames) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.writer = writer;
            this.likedCount = likedCount;
            this.replyCount = replyCount;
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

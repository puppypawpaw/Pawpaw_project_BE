package kr.co.pawpaw.api.dto.board;

import kr.co.pawpaw.api.dto.reply.ReplyDto.ReplyListDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


public class BoardDto {

    @Getter
    @Setter
    public static class BoardRegisterDto {
        private String title;
        private String content;
        @Builder
        public BoardRegisterDto(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

    @Getter
    @Setter
    public static class RegisterResponseDto {

        private String title;
        private String content;
        private String writer;
        private LocalDateTime createDate;
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

    @Getter
    @Setter
    public static class BoardUpdateDto {
        private String title;
        private String content;
//        private List<String> fileNames;

        @Builder
        public BoardUpdateDto(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

    @Getter
    @Setter
    public static class BoardListDto {
        private Long id;
        private String title;
        private String content;
        private String writer;
        private List<ReplyListDto> replyListDto;
        private int likedCount;
        private int replyCount;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        @Builder
        public BoardListDto(Long id, String title, String content, String writer, int likedCount, int replyCount, LocalDateTime createdDate, LocalDateTime modifiedDate, List<ReplyListDto> replyListDto) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.writer = writer;
            this.likedCount = likedCount;
            this.replyCount = replyCount;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
            this.replyListDto = replyListDto;
        }
    }

}

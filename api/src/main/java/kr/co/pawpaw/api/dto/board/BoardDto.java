package kr.co.pawpaw.api.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


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

}

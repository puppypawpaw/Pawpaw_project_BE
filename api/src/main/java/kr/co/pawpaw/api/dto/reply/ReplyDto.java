package kr.co.pawpaw.api.dto.reply;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class ReplyDto {

    @Getter
    @Schema(description = "댓글 등록 DTO")
    public static class ReplyRegisterDto {
        @Schema(description = "게시글 id", example = "1")
        private Long boardId;
        @Schema(description = "부모 댓글 id", nullable = true, example = "1")
        private Long parentId;
        @Schema(description = "댓글 내용", example = "댕댕이 귀엽네요")
        private String content;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "댓글 수정 DTO")
    public static class ReplyUpdateDto {
        @Schema(description = "댓글 내용", example = "냥이 귀엽네요")
        private String content;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "댓글 응답 DTO")
    public static class ReplyResponseDto {
        @Schema(description = "작성자 명")
        private String writer;
        @Schema(description = "게시글 id")
        private Long boardId;
        @Schema(description = "부모 댓글 id", nullable = true)
        private Long parentId;
        @Schema(description = "댓글 id")
        private Long replyId;
        @Schema(description = "댓글 내용")
        private String content;

        @Builder
        public ReplyResponseDto(String writer, Long boardId, Long parentId, Long replyId, String content) {
            this.writer = writer;
            this.boardId = boardId;
            this.parentId = parentId;
            this.replyId = replyId;
            this.content = content;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "댓글 리스트 DTO")
    public static class ReplyListDto {
        @Schema(description = "댓글 id")
        private Long id;
        @Schema(description = "댓글 내용")
        private String content;
        @Schema(description = "작성자 명")
        private String nickname;
        @Schema(description = "댓글 작성자 확인")
        private boolean replyWriter;
        @Schema(description = "유저 프로필 url")
        private String userImageUrl;
        @ArraySchema(schema = @Schema(description = "자식 댓글 리스트", nullable = true))
        private List<ReplyListDto> children = new ArrayList<>();

        public ReplyListDto(Long id, String content, String nickname, boolean replyWriter, String userImageUrl) {
            this.id = id;
            this.content = content;
            this.nickname = nickname;
            this.replyWriter = replyWriter;
            this.userImageUrl = userImageUrl;
        }
        public void setChildToParentReply(List<ReplyListDto> children){
            this.children = children;
        }
    }
}

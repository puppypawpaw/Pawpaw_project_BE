package kr.co.pawpaw.mysql.reply.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ReplyTest {

    private Reply reply = Reply.builder()
            .content("content")
            .build();

    @Test
    @DisplayName("댓글의 내용을 수정한다")
    void updateContent() throws Exception {
        //when
        reply.updateContent("updateContent");
        //then
        assertThat(reply.getContent()).isEqualTo("updateContent");
    }

}
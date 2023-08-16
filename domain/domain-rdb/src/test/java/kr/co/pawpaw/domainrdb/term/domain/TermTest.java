package kr.co.pawpaw.domainrdb.term.domain;

import helper.UpdateTermRequestBuilder;
import kr.co.pawpaw.domainrdb.term.dto.request.UpdateTermRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TermTest {
    @Test
    @DisplayName("update 메소드 테스트")
    void update() {
        //given
        Term term = Term.builder()
            .content("term content")
            .title("term title")
            .order(1L)
            .required(true)
            .build();

        UpdateTermRequest request = UpdateTermRequestBuilder.builder()
            .content("term content update")
            .title("term title update")
            .order(2L)
            .required(false)
            .build();

        //when
        term.update(request);

        //then
        assertThat(term.getContent()).isEqualTo(request.getContent());
        assertThat(term.getTitle()).isEqualTo(request.getTitle());
        assertThat(term.getOrder()).isEqualTo(request.getOrder());
        assertThat(term.getRequired()).isEqualTo(request.getRequired());
    }
}
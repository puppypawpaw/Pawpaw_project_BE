package kr.co.pawpaw.feignClient.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RecipientTest {
    @Test
    @DisplayName("deleteToHyphen 메서드 테스트")
    void deleteToHyphen() {
        //given
        String noHyphen = "01012332112";
        String hyphen = "010-1233-2112";
        Recipient recipient1 = Recipient.builder()
            .to(noHyphen)
            .build();

        Recipient recipient2 = Recipient.builder()
            .to(hyphen)
            .build();

        //when
        recipient1.deleteToHyphen();
        recipient2.deleteToHyphen();

        //then
        assertThat(recipient1.getTo()).isEqualTo(noHyphen);
        assertThat(recipient2.getTo()).isEqualTo(noHyphen);
    }
}
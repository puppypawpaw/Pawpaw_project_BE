package kr.co.pawpaw.api.dto.position;

import kr.co.pawpaw.mysql.position.Position;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PositionResponseTest {

    @Test
    void of() {
        //given
        Position position = Position.builder()
            .longitude(36.7)
            .latitude(36.8)
            .name("positionName")
            .build();

        //when
        PositionResponse result = PositionResponse.of(position);

        //then
        assertThat(result.getLatitude()).isEqualTo(position.getLatitude());
        assertThat(result.getLongitude()).isEqualTo(position.getLongitude());
        assertThat(result.getName()).isEqualTo(position.getName());
    }
}
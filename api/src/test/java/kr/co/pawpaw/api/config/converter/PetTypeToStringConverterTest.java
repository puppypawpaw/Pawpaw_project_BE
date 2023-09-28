package kr.co.pawpaw.api.config.converter;

import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PetTypeToStringConverter 의")
class PetTypeToStringConverterTest {
    @Nested
    @DisplayName("convert 메서드는")
    class Convert {
        private final PetTypeToStringConverter converter = new PetTypeToStringConverter();

        @ParameterizedTest
        @EnumSource(PetType.class)
        @DisplayName("PetType의 koreanName을 반환한다.")
        void petTypeReturnKoreanName(final PetType petType) {
            //given
            String expectedResult = petType.getKoreanName();

            //when
            String result = converter.convert(petType);

            //then
            assertThat(result).isEqualTo(expectedResult);
        }
    }
}
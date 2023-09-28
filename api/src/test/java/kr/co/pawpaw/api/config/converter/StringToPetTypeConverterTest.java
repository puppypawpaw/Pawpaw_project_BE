package kr.co.pawpaw.api.config.converter;

import kr.co.pawpaw.common.exception.pet.InvalidPetTypeException;
import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("StringToPetTypeConverter 의")
class StringToPetTypeConverterTest {
    @Nested
    @DisplayName("convert 메서드는")
    class Convert {
        private StringToPetTypeConverter converter = new StringToPetTypeConverter();
        @ParameterizedTest
        @EnumSource(PetType.class)
        @DisplayName("PetType의 koreanName 해당하는 String값이 아닌 경우 예외가 발생한다.")
        void InvalidPetTypeException(final PetType petType) {
            //given
            String petTypeString = petType.getKoreanName() + " ";

            //when
            assertThatThrownBy(() -> converter.convert(petTypeString)).isInstanceOf(InvalidPetTypeException.class);

            //then
        }
        @ParameterizedTest
        @EnumSource(PetType.class)
        @DisplayName("PetType의 name 해당하는 String값인 경우 예외가 발생한다.")
        void InvalidPetTypeNameException(final PetType petType) {
            //given
            String petTypeString = petType.name();

            //when
            assertThatThrownBy(() -> converter.convert(petTypeString)).isInstanceOf(InvalidPetTypeException.class);

            //then
        }

        @ParameterizedTest
        @DisplayName("PetType의 koreanName에 해당하는 String값의 경우 정상작동한다.")
        @EnumSource(PetType.class)
        void success(final PetType petType) {
            //given
            String petTypeString = petType.getKoreanName();

            //when
            PetType convertResult = converter.convert(petTypeString);

            //then
            assertThat(convertResult).isEqualTo(petType);
        }
    }
}
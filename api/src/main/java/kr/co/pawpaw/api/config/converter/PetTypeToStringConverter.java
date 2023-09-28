package kr.co.pawpaw.api.config.converter;

import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import org.springframework.core.convert.converter.Converter;

public class PetTypeToStringConverter implements Converter<PetType, String> {
    @Override
    public String convert(final PetType petType) {
        return petType.getKoreanName();
    }
}

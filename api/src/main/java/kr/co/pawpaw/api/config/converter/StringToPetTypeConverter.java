package kr.co.pawpaw.api.config.converter;

import kr.co.pawpaw.common.exception.pet.InvalidPetTypeException;
import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import org.springframework.core.convert.converter.Converter;

public class StringToPetTypeConverter implements Converter<String, PetType> {
    @Override
    public PetType convert(final String source) {
        try {
            return PetType.koreanNameOf(source);
        } catch (final IllegalArgumentException e) {
            throw new InvalidPetTypeException();
        }
    }
}

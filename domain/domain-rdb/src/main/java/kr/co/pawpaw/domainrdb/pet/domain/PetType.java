package kr.co.pawpaw.domainrdb.pet.domain;

import lombok.Getter;

@Getter
public enum PetType {
    DOG("강아지"),
    CAT("고양이"),
    FISH("물고기"),
    BIRD("새"),
    HAMSTER("햄스터"),
    RABBIT("토끼"),
    GUINEA_PIG("기니피그"),
    LIZARD("도마뱀"),
    FROG("개구리");

    private String koreanName;

    PetType(final String koreanName) {
        this.koreanName = koreanName;
    }

    public static PetType koreanNameOf(final String koreanName) {
        for (PetType type : PetType.values()) {
            if (type.koreanName.equals(koreanName)) {
                return type;
            }
        }

        throw new IllegalArgumentException();
    }
}

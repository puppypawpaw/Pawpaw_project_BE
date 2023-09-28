package kr.co.pawpaw.api.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import lombok.Getter;

@Getter
public class PetTypeResponse {
    @Schema(description = "반려동물 유형의 한글명", example="강아지")
    private String koreanName;

    @Schema(description = "반려동물 유형의 영문명", example="DOG")
    private String englishName;

    public static PetTypeResponse of(final PetType petType) {
        PetTypeResponse response = new PetTypeResponse();
        response.koreanName = petType.getKoreanName();
        response.englishName = petType.name();

        return response;
    }
}

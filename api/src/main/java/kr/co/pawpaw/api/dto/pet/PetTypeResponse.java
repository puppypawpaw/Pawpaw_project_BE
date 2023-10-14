package kr.co.pawpaw.api.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.pet.domain.PetType;
import lombok.Getter;

@Getter
public class PetTypeResponse {
    @Schema(description = "반려동물 유형", example="강아지")
    private PetType type;

    public static PetTypeResponse of(final PetType petType) {
        PetTypeResponse response = new PetTypeResponse();
        response.type = petType;

        return response;
    }
}

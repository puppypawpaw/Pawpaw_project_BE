package kr.co.pawpaw.api.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.pet.domain.Pet;
import kr.co.pawpaw.mysql.pet.domain.PetType;
import lombok.Getter;

@Getter
public class PetResponse {
    @Schema(description = "반려동물 아이디", example="12")
    private Long petId;
    @Schema(description = "반려동물 이름", example="루이")
    private String name;
    @Schema(description = "반려동물 유형의 한글명", example="강아지")
    private PetType petType;

    public static PetResponse of(final Pet pet) {
        PetResponse response = new PetResponse();
        response.petId = pet.getId();
        response.name = pet.getName();
        response.petType = pet.getPetType();
        return response;
    }
}

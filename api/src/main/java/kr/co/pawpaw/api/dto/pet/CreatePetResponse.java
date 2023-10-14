package kr.co.pawpaw.api.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.pet.domain.Pet;
import lombok.Getter;

@Getter
public class CreatePetResponse {
    @Schema(description = "반려동물 아이디", example="123")
    private Long id;

    public static CreatePetResponse of(final Pet pet) {
        CreatePetResponse response = new CreatePetResponse();

        response.id = pet.getId();

        return response;
    }
}

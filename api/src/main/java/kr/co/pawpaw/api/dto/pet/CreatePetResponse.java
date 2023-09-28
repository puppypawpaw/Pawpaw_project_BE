package kr.co.pawpaw.api.dto.pet;

import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import lombok.Getter;

@Getter
public class CreatePetResponse {
    private Long id;

    public static CreatePetResponse of(final Pet pet) {
        CreatePetResponse response = new CreatePetResponse();

        response.id = pet.getId();

        return response;
    }
}

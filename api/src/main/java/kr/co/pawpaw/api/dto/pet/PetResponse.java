package kr.co.pawpaw.api.dto.pet;

import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import lombok.Getter;

@Getter
public class PetResponse {
    private Long petId;
    private String name;
    private PetType petType;

    public static PetResponse of(final Pet pet) {
        PetResponse response = new PetResponse();
        response.petId = pet.getId();
        response.name = pet.getName();
        response.petType = pet.getPetType();
        return response;
    }
}

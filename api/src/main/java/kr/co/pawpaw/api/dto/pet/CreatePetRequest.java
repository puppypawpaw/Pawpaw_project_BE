package kr.co.pawpaw.api.dto.pet;

import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
public class CreatePetRequest {
    @NotBlank
    @Size(max=10)
    private String petName;
    @NotNull
    private PetType petType;
}
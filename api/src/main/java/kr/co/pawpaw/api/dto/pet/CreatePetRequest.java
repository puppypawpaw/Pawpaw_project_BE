package kr.co.pawpaw.api.dto.pet;

import kr.co.pawpaw.domainrdb.pet.domain.PetType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePetRequest {
    @NotBlank
    @Size(max=10)
    private String petName;
    @NotNull
    private PetType petType;
}
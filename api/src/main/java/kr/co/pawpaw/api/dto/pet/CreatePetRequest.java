package kr.co.pawpaw.api.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.pet.domain.Pet;
import kr.co.pawpaw.mysql.pet.domain.PetType;
import kr.co.pawpaw.mysql.user.domain.User;
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
    @Schema(description = "펫 이름", example = "루이")
    private String petName;
    @NotNull
    @Schema(description = "펫 유형", example = "강아지|고양이|물고기|새|햄스터|토끼|기니피그|도마뱀|개구리")
    private PetType petType;

    public Pet toEntity(final User parent) {
        return Pet.builder()
            .parent(parent)
            .name(petName)
            .petType(petType)
            .build();
    }
}
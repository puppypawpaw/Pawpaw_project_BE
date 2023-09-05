package kr.co.pawpaw.api.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "펫 이름")
    private String petName;
    @NotNull
    @Schema(description = "펫 유형")
    private PetType petType;
}
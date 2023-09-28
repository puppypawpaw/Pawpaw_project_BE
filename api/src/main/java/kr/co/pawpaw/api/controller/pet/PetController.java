package kr.co.pawpaw.api.controller.pet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.pet.CreatePetResponse;
import kr.co.pawpaw.api.dto.pet.PetResponse;
import kr.co.pawpaw.api.service.pet.PetService;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "pet")
@RestController
@RequestMapping("/api/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 반려동물 유형입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "POST",
        summary = "유저 반려동물 추가",
        description = "유저 반려동물 추가(단건)"
    )
    @PostMapping
    public ResponseEntity<CreatePetResponse> createPet(
        @AuthenticatedUserId final UserId userId,
        @RequestBody final CreatePetRequest request
    ) {
        return ResponseEntity.ok(petService.createPet(userId, request));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        )
    })
    @Operation(
        method = "GET",
        summary = "유저 반려동물 목록 조회",
        description = "유저 반려동물 목록 조회"
    )
    @GetMapping
    public ResponseEntity<List<PetResponse>> getPetList(
        @AuthenticatedUserId final UserId userId
    ) {
        return ResponseEntity.ok(petService.getPetList(userId));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 유저입니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 반려동물 입니다.",
            content = @Content
        )
    })
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(
        @AuthenticatedUserId final UserId userId,
        @PathVariable final Long petId
    ) {
        petService.deletePet(userId, petId);

        return ResponseEntity.noContent().build();
    }
}

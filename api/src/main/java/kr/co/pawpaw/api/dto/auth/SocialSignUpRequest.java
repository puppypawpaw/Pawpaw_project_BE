package kr.co.pawpaw.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.position.PositionRequest;
import kr.co.pawpaw.mysql.pet.domain.Pet;
import kr.co.pawpaw.mysql.user.domain.OAuth2Provider;
import kr.co.pawpaw.mysql.user.domain.User;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SocialSignUpRequest {
    @NotBlank @Schema(description = "redirect 시 제공했던 key 입력필요, key 유효기간 1일", example = "874a7470-a648-4b99-927c-9a2acc5f65a5")
    private String key;

    @NotBlank @Schema(description = "유저의 한줄 소개", example = "3살 너구리")
    private String briefIntroduction;

    @NotNull @Schema(description = "유저의 약관 동의 여부, 1, 2, 3번이 필수, 4번이 필수아님", example = "[1,2,3]")
    private List<Long> termAgrees;

    @Valid @NotNull @Schema(description = "유저의 초기 설정 위치")
    private PositionRequest position;

    @NotBlank @Schema(description = "소셜에서의 닉네임에서 변경하지 않더라도 입력해야됨", example = "수박이")
    private String nickname;

    @NotNull @Schema(description = "프로필 이미지가 있는지 없는지 여부. " +
        "이미지가 있는 경우 " +
        "요청에서 image필드가 존재하면 " +
        "그 이미지로 " +
        "존재하지 않으면 " +
        "소셜에서 제공하는 이미지로")
    private Boolean noImage;

    @Valid @NotNull @Size(min=1) @Schema(description = "반려동물 생성 요청, 1이상의 길이의 array필요")
    private List<CreatePetRequest> petInfos;

    public User toUser(
        final String email,
        final OAuth2Provider provider
    ) {
        return User.builder()
            .email(email)
            .password("")
            .nickname(nickname)
            .briefIntroduction(briefIntroduction)
            .position(position.toEntity())
            .provider(provider)
            .build();
    }

    public List<Pet> toPet(final User user) {
        return petInfos.stream()
            .map(petInfo -> Pet.builder()
                .name(petInfo.getPetName())
                .petType(petInfo.getPetType())
                .parent(user)
                .build()
            ).collect(Collectors.toList());
    }
}

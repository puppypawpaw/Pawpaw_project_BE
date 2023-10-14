package kr.co.pawpaw.api.dto.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.position.PositionRequest;
import kr.co.pawpaw.mysql.pet.domain.Pet;
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
public class SignUpRequest {
    @NotNull
    @Schema(description = "유저의 약관 동의 여부, 1, 2, 3번이 필수, 4번이 필수아님", example = "[1,2,3]")
    private List<Long> termAgrees;
    @NotBlank
    @Schema(description = "유저의 닉네임", example = "수박이")
    private String nickname;
    @NotBlank
    @Schema(description = "유저의 이메일", example = "zigzaag@pawpaw.com")
    private String email;
    @NotBlank
    @Schema(description = "유저의 비밀번호", example = "abcd1234")
    private String password;
    @Schema(description = "유저의 핸드폰 번호", example = "01012345678")
    private String phoneNumber;
    @NotNull
    @Valid
    @Schema(description = "유저의 초기 설정 위치")
    private PositionRequest position;
    @Valid
    @NotNull
    @Size(min=1)
    @Schema(description = "반려동물 생성 요청, 1이상의 길이의 array필요")
    private List<CreatePetRequest> petInfos;

    public User toUser(
        final String passwordEncoded,
        final String name
    ) {
        return User.builder()
            .email(email)
            .password(passwordEncoded)
            .name(name)
            .nickname(nickname)
            .phoneNumber(phoneNumber)
            .position(position.toEntity())
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

    public void deletePhoneNumberHyphen() {
        this.phoneNumber = this.phoneNumber.replaceAll("-", "");
    }
}

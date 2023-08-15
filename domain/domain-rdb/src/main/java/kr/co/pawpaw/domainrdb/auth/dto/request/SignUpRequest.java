package kr.co.pawpaw.domainrdb.auth.dto.request;


import kr.co.pawpaw.domainrdb.pet.Pet;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    @NotNull
    private List<Long> termAgrees;
    @NotBlank
    private String nickname;
    @NotBlank
    private String id;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirm;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String position;
    @NotNull
    @Size(min=1, max=10)
    private String petName;
    @NotNull
    @Size(min=1, max=50)
    private String petIntroduction;

    public User toUser(final String passwordEncoded) {
        return User.builder()
            .id(id)
            .password(passwordEncoded)
            .nickname(nickname)
            .phoneNumber(phoneNumber)
            .build();
    }

    public Pet toPet(final User user) {
        return Pet.builder()
            .name(petName)
            .introduction(petIntroduction)
            .parent(user)
            .build();
    }
}

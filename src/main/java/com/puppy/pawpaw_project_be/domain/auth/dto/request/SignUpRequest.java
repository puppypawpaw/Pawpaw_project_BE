package com.puppy.pawpaw_project_be.domain.auth.dto.request;

import com.puppy.pawpaw_project_be.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
    @NotBlank
    private String phoneNumber;

    @Builder
    public SignUpRequest(
        final String id,
        final String password,
        final String nickname,
        final String phoneNumber
    ) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    public User toEntity(final String passwordEncoded) {
        return User.builder()
            .id(id)
            .password(passwordEncoded)
            .nickname(nickname)
            .phoneNumber(phoneNumber)
            .build();
    }
}

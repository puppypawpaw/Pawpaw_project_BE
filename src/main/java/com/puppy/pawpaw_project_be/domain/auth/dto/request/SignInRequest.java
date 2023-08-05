package com.puppy.pawpaw_project_be.domain.auth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignInRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String password;

    @Builder
    public SignInRequest (
        final String id,
        final String password
    ) {
        this.id = id;
        this.password = password;
    }
}

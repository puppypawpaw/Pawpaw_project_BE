package kr.co.pawpaw.domainrdb.auth.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SignInRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String password;
}

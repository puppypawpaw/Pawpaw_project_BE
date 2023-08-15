package kr.co.pawpaw.domainrdb.auth.dto.request;

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
}

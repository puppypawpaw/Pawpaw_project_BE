package kr.co.pawpaw.api.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private long accessTokenLifeTime;
    private long refreshTokenLifeTime;
}
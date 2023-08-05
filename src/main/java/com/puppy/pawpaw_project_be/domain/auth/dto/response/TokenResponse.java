package com.puppy.pawpaw_project_be.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private long accessTokenLifeTime;
    private long refreshTokenLifeTime;

    @Builder
    public TokenResponse(
        final String accessToken,
        final String refreshToken,
        final long accessTokenLifeTime,
        final long refreshTokenLifeTime
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenLifeTime = accessTokenLifeTime;
        this.refreshTokenLifeTime = refreshTokenLifeTime;
    }
}
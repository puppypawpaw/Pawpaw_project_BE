package kr.co.pawpaw.api.application.auth;

import kr.co.pawpaw.api.util.cookie.CookieUtil;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainredis.auth.domain.TokenType;
import kr.co.pawpaw.domainredis.auth.service.command.RefreshTokenCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class SignOutService {
    private final RefreshTokenCommand refreshTokenCommand;

    public void signOut(
        final UserId userId,
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        deleteTokenCookie(request, response, TokenType.ACCESS);
        deleteTokenCookie(request, response, TokenType.REFRESH);

        refreshTokenCommand.deleteById(userId.getValue());
    }

    private void deleteTokenCookie(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final TokenType tokenType
    ) {
        CookieUtil.deleteCookie(request, response, tokenType.name());
    }
}

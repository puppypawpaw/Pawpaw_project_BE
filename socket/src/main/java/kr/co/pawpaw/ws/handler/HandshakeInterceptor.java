package kr.co.pawpaw.ws.handler;

import kr.co.pawpaw.ws.parser.JwtTokenParser;
import kr.co.pawpaw.ws.util.cookie.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    private final JwtTokenParser jwtTokenParser;

    @Override
    public boolean beforeHandshake(
        ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes
    ) throws Exception {
        String cookieString = request.getHeaders().getFirst("cookie");
        String accessToken = CookieUtil.getCookieValueFromCookies(cookieString, "ACCESS");

        if (jwtTokenParser.validateAccessToken(accessToken)) {
            attributes.put("userId", ((User) jwtTokenParser.extractAuthentication(accessToken).getPrincipal()).getUsername());
        }

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}

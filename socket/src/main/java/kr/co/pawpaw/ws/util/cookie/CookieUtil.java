package kr.co.pawpaw.ws.util.cookie;

import lombok.experimental.UtilityClass;

import javax.servlet.http.Cookie;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpCookie;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@UtilityClass
public class CookieUtil {
    public <T> T deserialize(
        final Cookie cookie,
        final Class<T> clazz
    ) {
        try {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(cookie.getValue());

            try (ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(decodedBytes))) {
                return clazz.cast(inputStream.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCookieValueFromCookies(
        final String cookieString,
        final String cookieName
    ) {
        if (Objects.nonNull(cookieString)) {
            List<HttpCookie> httpCookies = Arrays.stream(cookieString.split(";"))
                .map(cookie -> HttpCookie.parse(cookie).get(0))
                .collect(Collectors.toList());

            for (HttpCookie httpCookie : httpCookies) {
                if (httpCookie.getName().equals(cookieName)) {
                    return CookieUtil.deserialize(CookieUtil.convertHttpCookieToCookie(httpCookie), String.class);
                }
            }
        }

        return null;
    }

    public Cookie convertHttpCookieToCookie(final HttpCookie httpCookie) {
        Cookie cookie = new Cookie(httpCookie.getName(), httpCookie.getValue());

        if (httpCookie.getDomain() != null) {
            cookie.setDomain(httpCookie.getDomain());
        }

        if (httpCookie.getPath() != null) {
            cookie.setPath(httpCookie.getPath());
        }

        cookie.setSecure(httpCookie.getSecure());
        cookie.setHttpOnly(httpCookie.isHttpOnly());
        cookie.setMaxAge((int)httpCookie.getMaxAge());

        return cookie;
    }
}

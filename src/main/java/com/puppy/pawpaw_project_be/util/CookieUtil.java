package com.puppy.pawpaw_project_be.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

@UtilityClass
public class CookieUtil {
    public Optional<Cookie> getCookie(
        final HttpServletRequest request,
        final String name
    ) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public void addCookie(
        final HttpServletResponse response,
        final String name,
        final Object value,
        final long maxAge,
        final String domain,
        final String sameSite
    ) {
        response.addHeader("Set-Cookie", ResponseCookie.from(name, serialize(value))
            .domain(domain)
            .path("/")
            .maxAge(maxAge)
            .httpOnly(true)
            .secure(true)
            .sameSite(sameSite)
            .build()
            .toString());
    }

    public void deleteCookie(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String name,
        final String cookieDomain,
        final String sameSite
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    ResponseCookie responseCookie = ResponseCookie.from(name, "")
                        .path("/")
                        .maxAge(0)
                        .domain(cookieDomain)
                        .secure(true)
                        .sameSite(sameSite)
                        .build();
                    response.addHeader("Set-Cookie", responseCookie.toString());
                }
            }
        }
    }
    public <T> T deserialize(
        final Cookie cookie,
        final Class<T> clazz
    ) {
        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }

    public String serialize(final Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }
}

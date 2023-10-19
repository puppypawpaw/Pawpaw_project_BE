package kr.co.pawpaw.api.util.cookie;

import lombok.experimental.UtilityClass;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import java.io.*;
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
        ResponseCookie cookie = ResponseCookie.from(name, serialize(value))
            .maxAge(maxAge)
            .path("/")
            .domain(domain)
            .httpOnly(true)
            .secure(true)
            .sameSite(sameSite)
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteCookie(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String name,
        final String domain
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    ResponseCookie deleteCookie = ResponseCookie.from(name, "")
                        .maxAge(0)
                        .path("/")
                        .domain(domain)
                        .httpOnly(true)
                        .secure(true)
                        .build();
                    response.addHeader("Set-Cookie", deleteCookie.toString());
                }
            }
        }
    }
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

    public String serialize(final Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(object);
            }
            byte[] serializedBytes = byteArrayOutputStream.toByteArray();
            return Base64.getUrlEncoder().encodeToString(serializedBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

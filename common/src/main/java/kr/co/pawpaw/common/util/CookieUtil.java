package kr.co.pawpaw.common.util;

import lombok.experimental.UtilityClass;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        final int maxAge,
        final String domain
    ) {
        Cookie cookie = new Cookie(name, serialize(value));
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        response.addCookie(cookie);
    }

    public void deleteCookie(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String name
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
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

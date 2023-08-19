package kr.co.pawpaw.common.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CookieUtilTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCookie() {
        //given
        String cookieName1 = "cookie1";
        String cookieName2 = "cookie2";
        String cookieValue1 = "cookie1-value";
        String cookieValue2 = "cookie2-value";
        String cookieName3 = "cookie3";

        Cookie[] cookies = {
            new Cookie(cookieName1, cookieValue1),
            new Cookie(cookieName2, cookieValue2)
        };

        when(request.getCookies()).thenReturn(cookies);

        //when
        Optional<Cookie> result1 = CookieUtil.getCookie(request, cookieName1);
        Optional<Cookie> result2 = CookieUtil.getCookie(request, cookieName2);
        Optional<Cookie> result3 = CookieUtil.getCookie(request, cookieName3);

        //then
        verify(request, times(3)).getCookies();
        assertThat(result1.isPresent()).isTrue();
        assertThat(result2.isPresent()).isTrue();
        assertThat(result3.isPresent()).isFalse();
        assertThat(result1.get()).isEqualTo(cookies[0]);
        assertThat(result2.get()).isEqualTo(cookies[1]);
    }

    @Test
    void addCookie() {
        //given
        String name = "testCookie";
        String value = "testValue";
        int maxAge = 3600;
        String domain = "example.com";
        String sameSite = "Strict";

        //when
        CookieUtil.addCookie(response, name, value, maxAge, domain);
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie result = cookieCaptor.getValue();

        //then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getValue()).isEqualTo(CookieUtil.serialize(value));
        assertThat(result.getMaxAge()).isEqualTo(maxAge);
        assertThat(result.getDomain()).isEqualTo(domain);
    }

    @Test
    void deleteCookie() {
        //given
        String cookieName1 = "cookie1";
        String cookieName2 = "cookie2";
        String cookieValue1 = "cookie1-value";
        String cookieValue2 = "cookie2-value";

        Cookie[] cookies = {
            new Cookie(cookieName1, cookieValue1),
            new Cookie(cookieName2, cookieValue2)
        };

        when(request.getCookies()).thenReturn(cookies);

        //when
        CookieUtil.deleteCookie(request, response, cookieName1);
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie result = cookieCaptor.getValue();

        //then
        assertThat(result.getName()).isEqualTo(cookieName1);
        assertThat(result.getValue()).isEqualTo("");
        assertThat(result.getPath()).isEqualTo("/");
        assertThat(result.getMaxAge()).isEqualTo(0);
    }

    @Test
    void deserialize() {
        //given
        Cookie toSerializeValue = new Cookie("cookie", "cookie-value");
        String serializedValue = Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(toSerializeValue));
        Cookie serializedCookie = new Cookie("serializedCookie", serializedValue);

        //when
        Cookie result = CookieUtil.deserialize(serializedCookie, Cookie.class);

        //then
        assertThat(result.getName()).isEqualTo(toSerializeValue.getName());
        assertThat(result.getValue()).isEqualTo(toSerializeValue.getValue());
    }

    @Test
    void serialize() {
        //given
        Cookie toSerializeValue = new Cookie("cookie", "cookie-value");
        String serializedValue = Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(toSerializeValue));

        //when
        String result = CookieUtil.serialize(toSerializeValue);

        //then
        assertThat(result).isEqualTo(serializedValue);
    }
}
package kr.co.pawpaw.ws.application;

import org.springframework.stereotype.Service;

@Service
public class TestApplication {
    public String testMethod(final String payload) {
        return "hello " + payload;
    }
}

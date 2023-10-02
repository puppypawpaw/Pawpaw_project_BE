package kr.co.pawpaw.ws.controller.test;

import kr.co.pawpaw.ws.application.TestApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TestController {
    private final TestApplication testApplication;

    @MessageMapping("/test")
    @SendTo("/sub/test")
    public String putMove(
            @Payload final String payload
    ) {
        return testApplication.testMethod(payload);
    }
}
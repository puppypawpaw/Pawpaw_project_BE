package kr.co.pawpaw.feignClient.service;

import kr.co.pawpaw.feignClient.client.SmsClient;
import kr.co.pawpaw.feignClient.dto.Recipient;
import kr.co.pawpaw.feignClient.dto.SendSmsRequest;
import kr.co.pawpaw.feignClient.dto.SendSmsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SmsFeignServiceImplTest {
    @Mock
    private SmsClient smsClient;

    private SmsFeignServiceImpl smsFeignServiceImpl;

    @BeforeEach
    void setup() {
        smsFeignServiceImpl = new SmsFeignServiceImpl(smsClient, "01012345678", "serviceId");
    }

    @Test
    @DisplayName("sendSmsMessage 메소드 테스트")
    void sendSmsMessage() {
        //given
        Recipient recipient = Recipient.builder()
            .to("01087654321")
            .build();
        String content = "content";
        SendSmsRequest request = SendSmsRequest.builder()
            .messages(List.of(recipient))
            .type("SMS")
            .from("01012345678")
            .content(content)
            .build();
        String requestId = "requestId";
        String requestTime = String.valueOf(System.currentTimeMillis());
        String statusCode = "202";
        String statusName = "success";
        SendSmsResponse response = new SendSmsResponse(requestId, requestTime, statusCode, statusName);

        when(smsClient.sendMessage(any(String.class), any(SendSmsRequest.class))).thenReturn(response);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SendSmsRequest> requestCaptor = ArgumentCaptor.forClass(SendSmsRequest.class);
        //when
        SendSmsResponse result = smsFeignServiceImpl.sendSmsMessage(content, recipient);

        //then
        verify(smsClient).sendMessage(stringCaptor.capture(), requestCaptor.capture());
        String stringResult = stringCaptor.getValue();
        SendSmsRequest requestResult = requestCaptor.getValue();

        assertThat(stringResult).isEqualTo("serviceId");
        assertThat(requestResult.getMessages().size()).isEqualTo(1);
        assertThat(requestResult.getMessages().get(0)).isEqualTo(recipient);
        assertThat(requestResult.getFrom()).isEqualTo(request.getFrom());
        assertThat(requestResult.getType()).isEqualTo(request.getType());
        assertThat(requestResult.getContent()).isEqualTo(request.getContent());
        assertThat(result).isEqualTo(response);
    }
}
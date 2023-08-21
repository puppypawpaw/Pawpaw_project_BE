package kr.co.pawpaw.feignClient.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class NaverCloudSmsConfig {
    private final String accessKey;
    private final String secretKey;

    public NaverCloudSmsConfig(
        @Value("${feign.sms.naver.access-key}") final String accessKey,
        @Value("${feign.sms.naver.secret-key}") final String secretKey
    ) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private String makeSignature(
        final String method,
        final String url,
        final String timestamp
    ) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";

        String message = new StringBuilder()
            .append(method)
            .append(space)
            .append(url)
            .append(newLine)
            .append(timestamp)
            .append(newLine)
            .append(accessKey)
            .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(rawHmac);
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String url = requestTemplate.url();
            String method = requestTemplate.method();
            String timestamp = String.valueOf(System.currentTimeMillis());
            
            try {
                requestTemplate.header("Content-Type", "application/json; charset=utf-8");
                requestTemplate.header("x-ncp-apigw-timestamp", timestamp);
                requestTemplate.header("x-ncp-iam-access-key", accessKey);
                requestTemplate.header("x-ncp-apigw-signature-v2", makeSignature(method, url, timestamp));
                log.error(requestTemplate.toString());
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
                log.error(e.toString());
            }
        };
    }
}
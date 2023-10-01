package kr.co.pawpaw.mysql.sms.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private SmsCloudPlatform cloudPlatform;

    @Enumerated(value = EnumType.STRING)
    private SmsType type;

    @Enumerated(value = EnumType.STRING)
    private SmsUsagePurpose usagePurpose;

    private String recipient;
    private String title;
    private String content;
    private String requestId;
    private String statusCode;
    private String statusName;

    @Builder
    public SmsLog(
        final SmsType type,
        final SmsUsagePurpose usagePurpose,
        final SmsCloudPlatform cloudPlatform,
        final String recipient,
        final String title,
        final String content,
        final String requestId,
        final String statusCode,
        final String statusName
    ) {
        this.type = type;
        this.usagePurpose = usagePurpose;
        this.cloudPlatform = cloudPlatform;
        this.recipient = recipient;
        this.title = title;
        this.content = content;
        this.requestId = requestId;
        this.statusCode = statusCode;
        this.statusName = statusName;
    }
}

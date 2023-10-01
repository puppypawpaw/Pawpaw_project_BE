package kr.co.pawpaw.mysql.email.domain;

import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import kr.co.pawpaw.mysql.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;
    private String subject;
    @Column(columnDefinition="TEXT")
    private String text;
    @Enumerated(value = EnumType.STRING)
    private EmailType emailType;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User sender;

    @Builder
    public EmailLog(
        final String recipient,
        final String subject,
        final String text,
        final EmailType emailType,
        final User sender
    ) {
        this.recipient = recipient;
        this.subject = subject;
        this.text = text;
        this.emailType = emailType;
        this.sender = sender;
    }
}

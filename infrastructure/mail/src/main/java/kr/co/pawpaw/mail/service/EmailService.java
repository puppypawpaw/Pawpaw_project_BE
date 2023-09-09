package kr.co.pawpaw.mail.service;

import kr.co.pawpaw.mail.dto.SendEmailRequest;

public interface EmailService {
    void sendMail(final SendEmailRequest request);
}

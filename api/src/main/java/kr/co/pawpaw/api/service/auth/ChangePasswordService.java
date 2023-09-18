package kr.co.pawpaw.api.service.auth;

import kr.co.pawpaw.api.service.mail.MailService;
import kr.co.pawpaw.api.config.property.MailProperties;
import kr.co.pawpaw.api.dto.auth.ChangePasswordMailRequest;
import kr.co.pawpaw.api.dto.auth.ChangePasswordRequest;
import kr.co.pawpaw.api.util.mail.ChangePasswordMailContent;
import kr.co.pawpaw.api.util.mail.MailContent;
import kr.co.pawpaw.api.util.mail.MailUtil;
import kr.co.pawpaw.common.exception.auth.NotFoundChangePasswordTempKeyException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.email.domain.EmailType;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import kr.co.pawpaw.domainredis.auth.domain.ChangePasswordTempKey;
import kr.co.pawpaw.domainredis.auth.service.command.ChangePasswordTempKeyCommand;
import kr.co.pawpaw.domainredis.auth.service.query.ChangePasswordTempKeyQuery;
import kr.co.pawpaw.mail.dto.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangePasswordService {
    private final UserQuery userQuery;
    private final MailProperties mailProperties;
    private final ChangePasswordTempKeyCommand changePasswordTempKeyCommand;
    private final ChangePasswordTempKeyQuery changePasswordTempKeyQuery;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void sendChangePasswordMail(final ChangePasswordMailRequest request) {
        User user = findUserByRequest(request);

        ChangePasswordTempKey tempKey = createChangePasswordTempKey(user);

        MailContent mailContent = makeMailContentByTempKey(tempKey);

        SendEmailRequest sendMailRequest = makeSendEmailRequest(user, mailContent);

        mailService.sendMail(sendMailRequest, EmailType.CHANGE_PASSWORD, user);
    }

    @Transactional
    public void changePassword(final ChangePasswordRequest request) {
        ChangePasswordTempKey tempKey = changePasswordTempKeyQuery.findChangePasswordTempKeyByKey(request.getKey())
            .orElseThrow(NotFoundChangePasswordTempKeyException::new);

        User user = getUserByTempKey(tempKey);

        updateUserPassword(user, request.getPassword());
        changePasswordTempKeyCommand.delete(tempKey);
    }

    private User findUserByRequest(final ChangePasswordMailRequest request) {
        return userQuery.findByNameAndEmailAndProvider(request.getName(), request.getEmail(), null)
            .orElseThrow(NotFoundUserException::new);
    }

    private ChangePasswordMailContent makeMailContentByTempKey(final ChangePasswordTempKey tempKey) {
        return ChangePasswordMailContent.builder()
            .linkUrl(getLinkUrlByKey(tempKey.getKey()))
            .build();
    }

    private void updateUserPassword(
        final User user,
        final String newPassword
    ) {
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        user.updatePassword(encodedNewPassword);
    }

    private User getUserByTempKey(final ChangePasswordTempKey tempKey) {
        return userQuery.findByUserId(UserId.of(tempKey.getUserId()))
            .orElseThrow(NotFoundUserException::new);
    }

    private ChangePasswordTempKey createChangePasswordTempKey(final User user) {
        ChangePasswordTempKey changePasswordTempKey = ChangePasswordTempKey.builder()
            .userId(user.getUserId().getValue())
            .build();

        return changePasswordTempKeyCommand.save(changePasswordTempKey);
    }

    private String getLinkUrlByKey(final String key) {
        return mailProperties.getChangePasswordRedirectUrl() + key;
    }

    private static SendEmailRequest makeSendEmailRequest(
        final User user,
        final MailContent mailContent
    ) {
        String text = MailUtil.getText(mailContent);

        return SendEmailRequest.builder()
            .to(user.getEmail())
            .subject(mailContent.getSubject())
            .text(text)
            .build();
    }
}

package kr.co.pawpaw.api.service.auth;

import kr.co.pawpaw.api.dto.user.UserEmailResponse;
import kr.co.pawpaw.api.util.mask.MaskUtil;
import kr.co.pawpaw.api.util.time.TimeUtil;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindEmailService {
    private final UserQuery userQuery;

    public UserEmailResponse getUserEmail(final String name, final String phoneNumber) {
        return userQuery.findByNameAndPhoneNumber(name, phoneNumber)
            .map(this::getUserEmailResponse)
            .orElseThrow(NotFoundUserException::new);
    }

    private UserEmailResponse getUserEmailResponse(final User user) {
        return UserEmailResponse.of(
            MaskUtil.getMaskedEmail(user.getEmail()),
            TimeUtil.getYearMonthDay(user.getCreatedDate())
        );
    }
}

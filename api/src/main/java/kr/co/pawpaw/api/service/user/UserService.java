package kr.co.pawpaw.api.service.user;

import kr.co.pawpaw.api.dto.user.UpdateUserPositionRequest;
import kr.co.pawpaw.api.dto.user.UpdateUserProfileRequest;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.api.util.user.UserUtil;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.service.query.FileQuery;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserQuery userQuery;
    private final FileService fileService;
    private final FileQuery fileQuery;

    @Transactional(readOnly = true)
    public UserResponse whoAmI(final UserId userId) {
        return userQuery.findByUserId(userId)
            .map(this::getUserResponse)
            .orElseThrow(NotFoundUserException::new);
    }

    @Transactional
    public void updateUserPosition(
        final UserId userId,
        final UpdateUserPositionRequest request
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        user.updatePosition(request.getPosition().toEntity());
    }

    @Transactional
    public void updateUserImage(
        final UserId userId,
        final MultipartFile file
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);
        File newFile = fileService.saveFileByMultipartFile(file, userId);
        if (needDeleteFile(user.getUserImage())) {
            fileService.deleteFileByName(user.getUserImage().getFileName());
        }
        user.updateImage(newFile);
    }

    @Transactional
    public void updateUserProfile(
        final UserId userId,
        final UpdateUserProfileRequest updateUserProfileRequest
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        user.updateProfile(updateUserProfileRequest.getNickname(), updateUserProfileRequest.getBriefIntroduction());
    }

    private String getUserDefaultImageUrl() {
        return fileQuery.findByFileName(UserUtil.getUserDefaultImageName())
            .map(File::getFileUrl)
            .orElse(null);
    }

    private boolean needDeleteFile(final File file) {
        return Objects.nonNull(file) && file.getType().isNeedDelete();
    }

    private UserResponse getUserResponse(final User user) {
        return UserResponse.of(
            user,
            getUserImageUrl(user)
        );
    }

    private String getUserImageUrl(User user) {
        return Objects.nonNull(user.getUserImage()) ?
            user.getUserImage().getFileUrl() :
            getUserDefaultImageUrl();
    }
}

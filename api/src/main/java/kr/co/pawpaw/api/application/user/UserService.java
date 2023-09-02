package kr.co.pawpaw.api.application.user;

import kr.co.pawpaw.api.application.file.FileService;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.domain.UserImage;
import kr.co.pawpaw.domainrdb.user.service.command.UserImageCommand;
import kr.co.pawpaw.domainrdb.user.service.query.UserImageQuery;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserQuery userQuery;
    private final UserImageCommand userImageCommand;
    private final UserImageQuery userImageQuery;
    private final FileService fileService;
    private final EntityManager em;

    @Transactional(readOnly = true)
    public UserResponse whoAmI(final UserId userId) {
        return userQuery.findByUserId(userId)
            .map(this::getUserResponse)
            .orElseThrow(NotFoundUserException::new);
    }

    @Transactional
    public void updateUserImage(
        final UserId userId,
        final MultipartFile file
    ) {
        UserImage userImage = userImageQuery.findByUserId(userId)
            .orElse(null);

        File newFile = fileService.saveFileByMultipartFile(file, userId);

        if (userImage != null) {
            updateExistingUserImage(userImage, newFile);
        } else {
            createNewUserImage(userId, newFile);
        }
    }

    private void updateExistingUserImage(final UserImage userImage, final File newFile) {
        fileService.deleteFileByName(userImage.getFile().getFileName());
        userImage.updateFile(newFile);
    }

    private void createNewUserImage(final UserId userId, final File newFile) {
        UserImage newUserImage = UserImage.builder()
            .user(em.getReference(User.class, userId))
            .file(newFile)
            .build();

        userImageCommand.save(newUserImage);
    }

    private UserResponse getUserResponse(final User user) {
        String url = userImageQuery.findByUserId(user.getUserId())
            .map(this::getUserImageUrl)
            .orElse(null);

        return UserResponse.of(user, url);
    }

    private String getUserImageUrl(final UserImage userImage) {
        return fileService.getUrl(userImage.getFile().getFileName());
    }
}

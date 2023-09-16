package kr.co.pawpaw.api.application.user;

import kr.co.pawpaw.api.application.file.FileService;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserQuery userQuery;
    private final FileService fileService;

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
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        File newFile = fileService.saveFileByMultipartFile(file, userId);

        if (user.getUserImage() != null) {
            fileService.deleteFileByName(user.getUserImage().getFileName());
        }
        user.updateImage(newFile);
    }

    private UserResponse getUserResponse(final User user) {
        return UserResponse.of(user, user.getUserImage().getFileUrl());
    }
}

package kr.co.pawpaw.api.service.user;

import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.pet.CreatePetResponse;
import kr.co.pawpaw.api.dto.user.UpdateUserRequest;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.pet.service.command.PetCommand;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserQuery userQuery;
    private final FileService fileService;
    private final PetCommand petCommand;

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
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        File newFile = fileService.saveFileByMultipartFile(file, userId);

        if (user.getUserImage() != null) {
            fileService.deleteFileByName(user.getUserImage().getFileName());
        }
        user.updateImage(newFile);
    }

    @Transactional
    public void updateUser(
        final UserId userId,
        final UpdateUserRequest updateUserRequest
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        user.updateProfile(updateUserRequest.getNickname(), updateUserRequest.getBriefIntroduction());
    }

    @Transactional
    public CreatePetResponse createPet(
        final UserId userId,
        final CreatePetRequest request
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        return CreatePetResponse.of(petCommand.save(request.toEntity(user)));
    }

    private UserResponse getUserResponse(final User user) {
        return UserResponse.of(user, Optional.ofNullable(user.getUserImage())
            .map(File::getFileUrl)
            .orElse(null));
    }
}

package kr.co.pawpaw.api.service.pet;

import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.pet.CreatePetResponse;
import kr.co.pawpaw.api.dto.pet.PetResponse;
import kr.co.pawpaw.common.exception.pet.NotFoundPetException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.pet.service.command.PetCommand;
import kr.co.pawpaw.domainrdb.pet.service.query.PetQuery;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {
    private final UserQuery userQuery;
    private final PetQuery petQuery;
    private final PetCommand petCommand;

    @Transactional(readOnly = true)
    public List<PetResponse> getPetList(final UserId userId) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        return petQuery.findByParent(user)
            .stream()
            .map(PetResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deletePet(
        final UserId userId,
        final Long petId
    ) {
        User user = userQuery.findByUserId(userId)
            .orElseThrow(NotFoundUserException::new);

        Pet pet = petQuery.findByParentAndId(user, petId)
            .orElseThrow(NotFoundPetException::new);

        petCommand.delete(pet);
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
}

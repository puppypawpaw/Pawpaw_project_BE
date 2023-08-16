package kr.co.pawpaw.api.application.pet.command;

import kr.co.pawpaw.domainrdb.auth.dto.request.SignUpRequest;
import kr.co.pawpaw.domainrdb.pet.repository.PetRepository;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public void createPet(
        final SignUpRequest request,
        final MultipartFile petImage,
        final User user
    ) {
        petRepository.save(request.toPet(user));
    }

    /**
     * 테스트 용도
     * 모든 유저의 모든 pet을 삭제함
     */
    @Transactional
    public void deleteAll() {
        petRepository.deleteAll();
    }
}

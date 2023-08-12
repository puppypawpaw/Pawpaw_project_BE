package com.puppy.pawpaw_project_be.application.pet.command;

import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import com.puppy.pawpaw_project_be.domain.pet.domain.repository.PetRepository;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public void createPet(
        final SignUpRequest request,
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

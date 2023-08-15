package kr.co.pawpaw.api.application.pet.query;

import kr.co.pawpaw.domainrdb.pet.Pet;
import kr.co.pawpaw.domainrdb.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PetQuery {
    private final PetRepository petRepository;

    /**
     * 테스트 용도
     */
    @Transactional(readOnly = true)
    public List<Pet> getAllPetByUserId(final String id) {
        return petRepository.findAllByParentId(id);
    }
}

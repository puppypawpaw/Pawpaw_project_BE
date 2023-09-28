package kr.co.pawpaw.domainrdb.pet.service.query;

import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.pet.repository.PetRepository;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetQuery {
    private final PetRepository petRepository;

    public List<Pet> findByParent(final User user) {
        return petRepository.findByParent(user);
    }

}

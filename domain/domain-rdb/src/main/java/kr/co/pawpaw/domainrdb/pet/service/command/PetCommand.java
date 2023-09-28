package kr.co.pawpaw.domainrdb.pet.service.command;

import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetCommand {
    private final PetRepository petRepository;

    public List<Pet> saveAll(final Iterable<Pet> pets) {
        return petRepository.saveAll(pets);
    }

    public Pet save(final Pet pet) {
        return petRepository.save(pet);
    }
}

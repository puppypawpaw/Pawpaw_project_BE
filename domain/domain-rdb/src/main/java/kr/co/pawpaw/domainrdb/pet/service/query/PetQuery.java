package kr.co.pawpaw.domainrdb.pet.service.query;

import kr.co.pawpaw.domainrdb.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetQuery {
    private final PetRepository petRepository;

}

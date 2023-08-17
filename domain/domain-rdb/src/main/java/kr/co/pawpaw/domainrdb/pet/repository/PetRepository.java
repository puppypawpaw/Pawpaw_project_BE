package kr.co.pawpaw.domainrdb.pet.repository;

import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}

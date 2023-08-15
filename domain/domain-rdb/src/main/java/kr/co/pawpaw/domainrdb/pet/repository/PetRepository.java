package kr.co.pawpaw.domainrdb.pet.repository;

import kr.co.pawpaw.domainrdb.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByParentId(final String id);
}

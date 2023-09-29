package kr.co.pawpaw.domainrdb.pet.repository;

import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByParent(final User parent);

    Optional<Pet> findByParentAndId(final User parent, final Long id);
}

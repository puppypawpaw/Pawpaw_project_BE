package kr.co.pawpaw.mysql.pet.repository;

import kr.co.pawpaw.mysql.pet.domain.Pet;
import kr.co.pawpaw.mysql.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByParent(final User parent);

    Optional<Pet> findByParentAndId(final User parent, final Long id);
}

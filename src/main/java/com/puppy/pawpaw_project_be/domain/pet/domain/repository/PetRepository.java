package com.puppy.pawpaw_project_be.domain.pet.domain.repository;

import com.puppy.pawpaw_project_be.domain.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByParentId(final String id);
}

package kr.co.pawpaw.domainrdb.pet.repository;

import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PetRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        petRepository.deleteAll();
    }

    @Test
    @DisplayName("저장 불러오기 테스트")
    void save() {
        //given
        User user1 = User.builder().build();

        User user2 = User.builder().build();

        userRepository.saveAll(List.of(user1, user2));

        Pet pet1 = Pet.builder()
            .parent(user1)
            .name("petName1")
            .introduction("introduction1")
            .build();

        Pet pet2 = Pet.builder()
            .parent(user1)
            .name("petName2")
            .introduction("introduction2")
            .build();

        Pet pet3 = Pet.builder()
            .parent(user2)
            .name("petName3")
            .introduction("introduction3")
            .build();

        petRepository.saveAll(List.of(pet1, pet2, pet3));

        //when
        Optional<Pet> result1 = petRepository.findById(pet1.getId());
        Optional<Pet> result2 = petRepository.findById(pet2.getId());
        Optional<Pet> result3 = petRepository.findById(pet3.getId());

        //then
        assertThat(result1.isPresent()).isTrue();
        assertThat(result2.isPresent()).isTrue();
        assertThat(result3.isPresent()).isTrue();
        assertThat(result1.orElse(null)).isEqualTo(pet1);
        assertThat(result1.orElse(null)).isNotEqualTo(pet2);
        assertThat(result1.orElse(null)).isNotEqualTo(pet3);
        assertThat(result2.orElse(null)).isEqualTo(pet2);
        assertThat(result2.orElse(null)).isNotEqualTo(pet1);
        assertThat(result2.orElse(null)).isNotEqualTo(pet3);
        assertThat(result3.orElse(null)).isEqualTo(pet3);
        assertThat(result3.orElse(null)).isNotEqualTo(pet2);
        assertThat(result3.orElse(null)).isNotEqualTo(pet1);
    }
}
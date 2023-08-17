package kr.co.pawpaw.domainrdb.pet.service.command;

import kr.co.pawpaw.domainrdb.pet.domain.Pet;
import kr.co.pawpaw.domainrdb.pet.repository.PetRepository;
import kr.co.pawpaw.domainrdb.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PetCommandTest {
    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetCommand petCommand;

    @Test
    void saveAll() {
        //given
        User parent = User.builder().build();
        Pet pet = Pet.builder()
            .introduction("pet 소개")
            .name("pet 이름")
            .parent(parent)
            .build();
        List<Pet> input = List.of(pet);


        when(petCommand.saveAll(eq(input))).thenReturn(input);
        //when
        List<Pet> result = petCommand.saveAll(input);

        //then
        verify(petRepository).saveAll(input);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(pet);
    }
}
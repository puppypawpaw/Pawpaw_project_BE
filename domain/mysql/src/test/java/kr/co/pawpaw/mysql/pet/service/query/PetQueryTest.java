package kr.co.pawpaw.mysql.pet.service.query;

import kr.co.pawpaw.mysql.pet.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PetQueryTest {
    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetQuery petQuery;

    @Test
    void temp() {

    }
}
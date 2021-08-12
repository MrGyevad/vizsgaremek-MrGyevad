package hu.progmasters.animalShelter.IT;

import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.CatCommand;
import hu.progmasters.animalShelter.dto.CatInfo;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.service.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatServiceTest {

    @Mock
    CatRepository catRepository;

    @InjectMocks
    CatService catservice;

    private Cat cat1;
    private Cat cat2;
    private Dog dog1;
    private CatCommand command1;
    private CatCommand command2;
    private CatInfo info1;
    private CatInfo info2;


    @BeforeEach
    void init() {
        cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        cat2 = new Cat(2, "Ribizli", 5, "Halfear", Gender.PUSSY, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
        dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        command1 = new CatCommand("Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false);
        command2 = new CatCommand("Ribizli", 5, "Halfear", Gender.PUSSY, LocalDateTime.now(), true, false);
        info1 = new CatInfo(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false);
        info1 = new CatInfo(2, "Ribizli", 5, "Halfear", Gender.PUSSY, LocalDateTime.now(), true, false);
    }

    @Test
    void testList_atStart_emptyList() {
        when(catRepository.findAll()).thenReturn(List.of());
        assertThat(catservice.findAllCats().isEmpty());

        verify(catRepository, times(1)).findAll();
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    void testList_singleCatSaved_singleCatInList() {
        when(catRepository.save(cat1)).thenReturn(cat1);
        when(catRepository.findAll()).thenReturn(List.of(cat1));
        assertThat(catservice.findAllCats())
                .hasSize(1)
                .containsExactly(info1);
        verify(catRepository, times(1)).save(any());
        verify(catRepository, times(1)).findAll();
        verifyNoMoreInteractions(catRepository);

    }
}

package hu.progmasters.animalShelter.IT;

import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.DogCommand;
import hu.progmasters.animalShelter.dto.DogInfo;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import hu.progmasters.animalShelter.service.DogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DogServiceTest {

    @Mock
    DogRepository dogRepository;

    @Mock
    CatRepository catRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    DogService dogService;

    @BeforeEach
    void init(){
        dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        dog2 = new Dog(2, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
        dogCommand1 = new DogCommand("Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false);
        dogCommand2 = new DogCommand("Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false);
        updateCommand1 = new DogCommand("Digi", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false);
    }

    @Test
    void testSaveDog_SuccessfulSave(){
        when(dogRepository.save(any()))
                .thenReturn(new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null)));
        assertEquals(dogService.saveDog(dogCommand1), modelMapper.map(dogRepository.save(dog1), DogInfo.class));
        verify(dogRepository, times(1)).save(dog1);
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    void testUpdateDog_SuccessfulUpdate(){
        when(dogRepository.update(any()))
                .thenReturn(new Dog(2, "Digi", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null)));
        Dog dog2 = new Dog(2, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
                dogService.saveDog(dogCommand2);
        dog2.setId(2);
        assertEquals(dogService.updateDog(2, updateCommand1), modelMapper.map(dogRepository.update(dog2), DogInfo.class));
        verify(dogRepository, times(1)).update(dog1);
        verifyNoMoreInteractions(dogRepository);
    }

    private Dog dog1;
    private DogCommand dogCommand1;
    private Dog dog2;
    private DogCommand dogCommand2;
    private DogCommand updateCommand1;


}

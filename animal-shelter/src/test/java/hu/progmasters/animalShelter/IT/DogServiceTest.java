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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

    @Mock
    DogService dogService;

    @BeforeEach
    void init(){
        dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        dog2 = new Dog(2, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
        dogForUpdate = new Dog(1, "Digi", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        dogCommand1 = new DogCommand("Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false);
        dogCommand2 = new DogCommand("Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false);
        updateCommand1 = new DogCommand("Digi", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false);
        dogInfo1 = modelMapper.map(dog1, DogInfo.class);
        dogInfo2 = modelMapper.map(dog2, DogInfo.class);
    }

    @Test
    @Transactional
    void testSaveDog_SuccessfulSave(){
        when(dogRepository.save(any()))
                .thenReturn(new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null)));
        assertEquals(dogService.saveDog(dogCommand1), modelMapper.map(dogRepository.save(dog1), DogInfo.class));
        verify(dogRepository, times(1)).save(dog1);
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    @Transactional
    void testUpdateDog_SuccessfulUpdate(){
        Dog digi = new Dog(1, "Digi", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        when(dogRepository.findById(any()))
                .thenReturn(Optional.of(digi));
        when(dogRepository.update(any()))
                .thenReturn(digi);
        when(dogRepository.save(any())).thenReturn(dogForUpdate);
        assertEquals("Digi", dogService.updateDog(1, updateCommand1).getName());
        verify(dogRepository, times(1)).update(dog1);
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    @Transactional
    void testWhoNeedsAWalk(){
        String ldt = "2021-08-03 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        when(dogRepository.findById(any())).thenReturn(Optional.of(new Dog(null, "Sirion", 6, "Mudi", Gender.SIRE, dateTime, true, false, new BestFriend(1, null, null))));
        when(dogRepository.findAll()).thenReturn(List.of(new Dog(null, "Sirion", 6, "Mudi", Gender.SIRE, dateTime, true, false, new BestFriend(1, null, null))));
        Dog toSave = new Dog(null, "Sirion", 6, "Mudi", Gender.SIRE, dateTime, true, false, new BestFriend(1, null, null));
        dogService.saveDog(dogCommand1);
        dogRepository.findById(1).get().setLastWalk(dateTime);
        assertEquals(dateTime, dogRepository.findById(1).get().getLastWalk());
        assertEquals(1, dogService.findAllDogs().size());
        assertEquals(1, dogService.whoNeedsAWalk().size());

    }

    private Dog dog1;
    private DogCommand dogCommand1;
    private Dog dog2;
    private DogCommand dogCommand2;
    private DogCommand updateCommand1;
    private DogInfo dogInfo1;
    private DogInfo dogInfo2;
    private Dog dogForUpdate;


}

package hu.progmasters.animalShelter.unit;

import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.*;
import hu.progmasters.animalShelter.exception.DogNotFoundException;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import hu.progmasters.animalShelter.service.DogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DogServiceTest {

    DogRepository dogRepository = mock(DogRepository.class);
    BestFriendRepository bestFriendRepository = mock(BestFriendRepository.class);
    CatRepository catRepository = mock(CatRepository.class);
    ModelMapper modelMapper = new ModelMapper();

    DogService dogService = new DogService(dogRepository, catRepository, bestFriendRepository, modelMapper);

    private Dog dog1;
    private DogCommand dogCommand1;
    private Dog dog2;
    private Dog dog3;
    private DogCommand dogCommand2;
    private DogCommand updateCommand1;
    private DogInfo dogInfo1;
    private DogInfo dogInfo2;
    private DogInfo dogInfo3;
    private Dog dogForUpdate;
    private BestFriendInfo bestFriendInfo1;
    private BestFriendInfo bestFriendInfo2;
    private CatInfo catInfo1;
    private CatInfo catInfo2;

    @BeforeEach
    void init(){
        String ldt = "2021-08-13 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, dateTime, true, false, new BestFriend(1, null, null));
        dog2 = new Dog(2, "Diego", 11, "Maltese", Gender.SIRE, dateTime, true, false, new BestFriend(2, null, null));
        dog3 = new Dog(1, "Digi", 11, "Maltese", Gender.SIRE, dateTime, true, false, new BestFriend(1, null, null));
        dogForUpdate = new Dog(1, "Digi", 11, "Maltese", Gender.SIRE, dateTime, true, false, new BestFriend(1, null, null));
        dogCommand1 = new DogCommand("Sirion", 6, "Mudi", Gender.SIRE, dateTime, true, false);
        dogCommand2 = new DogCommand("Diego", 11, "Maltese", Gender.SIRE, dateTime, true, false);
        updateCommand1 = new DogCommand("Digi", 11, "Maltese", Gender.SIRE, dateTime, true, false);
        dogInfo1 = new DogInfo(1, "Sirion", 6, "Mudi", Gender.SIRE, dateTime, true, false);
        dogInfo2 = new DogInfo(2, "Diego", 11, "Maltese", Gender.SIRE, dateTime, true, false);
        dogInfo3 = new DogInfo(1, "Digi", 11, "Maltese", Gender.SIRE, dateTime, true, false);
        catInfo1 = new CatInfo(1, "Lucifer", 10, "Giant", Gender.TOM,
                dateTime, true, false);
        catInfo2 = new CatInfo(2, "Ribizli", 5, "Halfear", Gender.PUSSY,
                dateTime, true, false);
        bestFriendInfo1 = new BestFriendInfo(1, catInfo1, dogInfo1);
        bestFriendInfo2 = new BestFriendInfo(2, catInfo2, dogInfo2);

    }

    @Test
    void testList_atStart_emptyList() {
        when(dogRepository.findAll()).thenReturn(List.of());
        assertTrue(dogService.findAllDogs().isEmpty());
        verify(dogRepository, times(1)).findAll();
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    @Transactional
    void testSaveDog_SuccessfulSave(){
        when(dogRepository.save(any()))
                .thenReturn(dog1);
        when(dogRepository.update(any()))
                .thenReturn(dog1);
        DogInfo savedDog= dogService.saveDog(dogCommand1);
        dogInfo1.setLastWalk(savedDog.getLastWalk());
        assertEquals(dogInfo1, savedDog);
        verify(dogRepository, times(1)).save(any());
        verify(dogRepository, times(1)).update(any());
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    void testList_singleDogSaved_singleDogInList() {
        when(dogRepository.save(any())).thenReturn(dog1);
        when(dogRepository.update(any())).thenReturn(dog1);
        when(dogRepository.findAll()).thenReturn(List.of(dog1));
        DogInfo info = dogService.saveDog(dogCommand1);
        assertThat(dogService.findAllDogs())
                .hasSize(1)
                .containsExactly(info);
        verify(dogRepository, times(1)).save(any());
        verify(dogRepository, times(1)).update(any());
        verify(dogRepository, times(1)).findAll();
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
        verify(dogRepository, times(1)).update(any());
        verify(dogRepository, times(2)).findById(any());
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    @Transactional
    void testWhoNeedsAWalk_oneDogNeedsAWalk(){
        String ldt = "2021-08-03 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        Dog sirion = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, dateTime, true, false, new BestFriend(1, null, null));
        when(dogRepository.findAll()).thenReturn(List.of(sirion));
        when(dogRepository.save(any())).thenReturn(sirion);
        when(dogRepository.update(any())).thenReturn(sirion);
        dogCommand1.setLastWalk(dateTime);
        DogInfo savedDog = dogService.saveDog(dogCommand1);
        assertEquals(dateTime, savedDog.getLastWalk());
        assertEquals(1, dogService.whoNeedsAWalk().size());
        assertEquals(1, dogService.findAllDogs().size());

        verify(dogRepository, times(1)).save(any());
        verify(dogRepository, times(1)).update(any());
        verify(dogRepository, times(2)).findAll();
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    @Transactional
    void testWhoNeedsAWalk_noOneNeedsAWalk(){
        when(dogRepository.findAll()).thenReturn(List.of(dog1));
        when(dogRepository.save(any())).thenReturn(dog1);
        when(dogRepository.update(any())).thenReturn(dog1);
        dogService.saveDog(dogCommand1);
        dog1.setLastWalk(LocalDateTime.now());
        assertEquals(1, dogService.findAllDogs().size());
        assertEquals(0, dogService.whoNeedsAWalk().size());

        verify(dogRepository, times(1)).save(any());
        verify(dogRepository, times(1)).update(any());
        verify(dogRepository, times(2)).findAll();
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    @Transactional
    void testFindById_SuccessfulFind(){
        when(dogRepository.findById(1)).thenReturn(Optional.of(dog1));
        when(dogRepository.save(any()))
                .thenReturn(dog1);
        when(dogRepository.update(any()))
                .thenReturn(dog1);
        DogInfo savedDog= dogService.saveDog(dogCommand1);
        dogInfo1.setLastWalk(savedDog.getLastWalk());
        assertEquals(dogInfo1, dogService.findById(1));

        verify(dogRepository, times(1)).save(any());
        verify(dogRepository, times(1)).update(any());
        verify(dogRepository, times(1)).findById(1);
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    @Transactional
    void testFindById_DogNotFound(){
        when(dogRepository.findById(3)).thenThrow(new DogNotFoundException());
        when(dogRepository.findById(1)).thenReturn(Optional.of(dog1));
        when(dogRepository.save(any())).thenReturn(dog1);
        when(dogRepository.update(any())).thenReturn(dog1);
        DogInfo savedDog = dogService.saveDog(dogCommand1);
        dogInfo1.setLastWalk(savedDog.getLastWalk());
        assertEquals(dogInfo1, dogService.findById(1));
        assertThrows(DogNotFoundException.class, () -> {
            dogService.findById(3);
        });

        verify(dogRepository, times(1)).save(any());
        verify(dogRepository, times(1)).update(any());
        verify(dogRepository, times(2)).findById(any());
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    void testWalkTheDog_successfulWalk() throws InterruptedException {
        DogInfo dog1Info = modelMapper.map(dog1, DogInfo.class);
        Dog dog1notWalked;
        dog1notWalked = modelMapper.map(dog1Info, Dog.class);
        dog1.setLastWalk(LocalDateTime.now());
        when(dogRepository.save(any())).thenReturn(dog1notWalked);
        when(dogRepository.update(any())).thenReturn(dog1notWalked);
        when(dogRepository.walkMeBoy(any())).thenReturn(dog1);
        DogInfo savedDog = dogService.saveDog(dogCommand1);
        DogInfo walkedDog = dogService.walkTheDog(1);
        assertNotEquals(savedDog.getLastWalk(), walkedDog.getLastWalk());

        verify(dogRepository, times(1)).save(any());
        verify(dogRepository, times(1)).update(any());
        verify(dogRepository, times(1)).walkMeBoy(1);
        verifyNoMoreInteractions(dogRepository);
    }



}

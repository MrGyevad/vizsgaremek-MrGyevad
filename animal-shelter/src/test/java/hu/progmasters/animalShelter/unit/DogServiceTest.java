package hu.progmasters.animalShelter.unit;

import hu.progmasters.animalShelter.domain.AnimalShelter;
import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.*;
import hu.progmasters.animalShelter.exception.DogNotFoundException;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import hu.progmasters.animalShelter.service.AnimalShelterService;
import hu.progmasters.animalShelter.service.DogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DogServiceTest {

    DogRepository dogRepository = mock(DogRepository.class);
    AnimalShelterService animalShelterService = mock(AnimalShelterService.class);
    BestFriendRepository bestFriendRepository = mock(BestFriendRepository.class);
    ModelMapper modelMapper = new ModelMapper();

    DogService dogService = new DogService(dogRepository, animalShelterService, bestFriendRepository, modelMapper);

    private AnimalShelter animalShelter;
    private Dog dog1;
    private DogCommand dogCommand1;
    private DogCommand updateCommand1;
    private DogInfo dogInfo1;
    private Dog dogForUpdate;

    @BeforeEach
    void init() {
        LocalDateTime dateTime = LocalDateTime.now().minusHours(1);
        animalShelter = new AnimalShelter(1, "HopeForPaws", new ArrayList<>(), new ArrayList<>());
        dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, dateTime, true, false, new BestFriend(1, null, null), new AnimalShelter());
        dogForUpdate = new Dog(1, "Digi", 11, "Maltese", Gender.SIRE, dateTime, true, false, new BestFriend(1, null, null), new AnimalShelter());
        dogCommand1 = new DogCommand("Sirion", 6, "Mudi", Gender.SIRE, dateTime, true, false, 1);
        updateCommand1 = new DogCommand("Digi", 11, "Maltese", Gender.SIRE, dateTime, true, false, 1);
        dogInfo1 = new DogInfo(1, "Sirion", 6, "Mudi", Gender.SIRE, dateTime, true, false, null);
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
    void testSaveDog_SuccessfulSave() {
        when(dogRepository.save(any()))
                .thenReturn(dog1);
        when(dogRepository.update(any()))
                .thenReturn(dog1);
        DogInfo savedDog = dogService.saveDog(dogCommand1);
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
    void testUpdateDog_SuccessfulUpdate() {
        when(dogRepository.findById(any()))
                .thenReturn(Optional.of(dogForUpdate));
        when(dogRepository.update(any()))
                .thenReturn(dogForUpdate);
        when(dogRepository.save(any())).thenReturn(dog1);
        when(animalShelterService.findByIdForService(any())).thenReturn(animalShelter);
        dogService.saveDog(dogCommand1);
        assertEquals("Digi", dogService.updateDog(1, updateCommand1).getName());
        verify(dogRepository, times(2)).update(any());
        verify(dogRepository, times(2)).findById(any());
        verify(dogRepository, times(1)).save(any());
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    @Transactional
    void testWhoNeedsAWalk_oneDogNeedsAWalk() {
        LocalDateTime dateTime = LocalDateTime.now().minusHours(8);
        Dog sirion = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, dateTime, true, false, new BestFriend(1, null, null), new AnimalShelter());
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
    void testWhoNeedsAWalk_noOneNeedsAWalk() {
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
    void testFindById_SuccessfulFind() {
        when(dogRepository.findById(1)).thenReturn(Optional.of(dog1));
        when(dogRepository.save(any()))
                .thenReturn(dog1);
        when(dogRepository.update(any()))
                .thenReturn(dog1);
        DogInfo savedDog = dogService.saveDog(dogCommand1);
        dogInfo1.setLastWalk(savedDog.getLastWalk());
        assertEquals(dogInfo1, dogService.findById(1));

        verify(dogRepository, times(1)).save(any());
        verify(dogRepository, times(1)).update(any());
        verify(dogRepository, times(1)).findById(1);
        verifyNoMoreInteractions(dogRepository);
    }

    @Test
    @Transactional
    void testFindById_DogNotFound() {
        when(dogRepository.findById(3)).thenThrow(new DogNotFoundException());
        when(dogRepository.findById(1)).thenReturn(Optional.of(dog1));
        when(dogRepository.save(any())).thenReturn(dog1);
        when(dogRepository.update(any())).thenReturn(dog1);
        DogInfo savedDog = dogService.saveDog(dogCommand1);
        dogInfo1.setLastWalk(savedDog.getLastWalk());
        assertEquals(dogInfo1, dogService.findById(1));
        assertThrows(DogNotFoundException.class, () -> dogService.findById(3));

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
        when(dogRepository.save(dog1notWalked)).thenReturn(dog1notWalked);
        when(dogRepository.save(dog1)).thenReturn(dog1);
        when(dogRepository.update(any())).thenReturn(dog1notWalked);
        when(dogRepository.findById(1)).thenReturn(Optional.of(dog1));
        DogInfo savedDog = dogService.saveDog(dogCommand1);
        DogInfo walkedDog = dogService.walkTheDog(1);
        assertNotEquals(savedDog.getLastWalk(), walkedDog.getLastWalk());

        verify(dogRepository, times(2)).save(any());
        verify(dogRepository, times(1)).update(any());
        verify(dogRepository, times(2)).findById(1);
        verifyNoMoreInteractions(dogRepository);
    }
}

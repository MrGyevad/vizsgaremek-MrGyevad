package hu.progmasters.animalShelter.IT;

import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.CatCommand;
import hu.progmasters.animalShelter.dto.CatInfo;
import hu.progmasters.animalShelter.dto.DogInfo;
import hu.progmasters.animalShelter.exception.CatNotFoundException;
import hu.progmasters.animalShelter.exception.NoBestFriendException;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import hu.progmasters.animalShelter.service.CatService;
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
public class CatServiceTest {

    DogRepository dogRepository = mock(DogRepository.class);
    BestFriendRepository bestFriendRepository = mock(BestFriendRepository.class);
    CatRepository catRepository = mock(CatRepository.class);
    ModelMapper modelMapper = new ModelMapper();

    CatService catService = new CatService(catRepository, dogRepository, bestFriendRepository, modelMapper);

    private Cat cat1;
    private Cat cat2;
    private Cat cat3;
    private Cat catForUpdate;
    private Dog dog1;
    private DogInfo dogInfo1;
    private CatCommand catCommand1;
    private CatCommand updateCommand1;
    private CatCommand catCommand2;
    private CatInfo catInfo1;
    private CatInfo catInfo2;


    @BeforeEach
    void init() {
        cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM,
                LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        catForUpdate = new Cat(1, "Luci", 10, "Giant", Gender.TOM,
                LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        cat2 = new Cat(2, "Ribizli", 5, "Halfear", Gender.PUSSY,
                LocalDateTime.now(), true, false, new BestFriend(2, null, null));
        dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE,
                LocalDateTime.now(), true, false, new BestFriend(3, cat3, dog1));
        dogInfo1 = new DogInfo(1, "Sirion", 6, "Mudi", Gender.SIRE,
                LocalDateTime.now(), true, false);
        catCommand1 = new CatCommand("Lucifer", 10, "Giant", Gender.TOM,
                LocalDateTime.now(), true, false);
        updateCommand1 = new CatCommand("Luci", 10, "Giant", Gender.TOM,
                LocalDateTime.now(), true, false);
        catCommand2 = new CatCommand("Ribizli", 5, "Halfear", Gender.PUSSY,
                LocalDateTime.now(), true, false);
        catInfo1 = new CatInfo(1, "Lucifer", 10, "Giant", Gender.TOM,
                LocalDateTime.now(), true, false);
        catInfo2 = new CatInfo(2, "Ribizli", 5, "Halfear", Gender.PUSSY,
                LocalDateTime.now(), true, false);
        cat3 = new Cat(3, "Retek", 4, "Ginger", Gender.TOM,
                LocalDateTime.now(), true, false, new BestFriend(3, cat3, dog1));
    }

    @Test
    void testList_atStart_emptyList() {
        when(catRepository.findAll()).thenReturn(List.of());
        assertTrue(catService.findAllCats().isEmpty());
        verify(catRepository, times(1)).findAll();
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    void testList_singleCatSaved_singleCatInList() {
        when(catRepository.save(any())).thenReturn(cat1);
        when(catRepository.findAll()).thenReturn(List.of(cat1));
        CatInfo info = catService.saveCat(catCommand1);
        assertThat(catService.findAllCats())
                .hasSize(1)
                .containsExactly(info);
        verify(catRepository, times(1)).save(any());
        verify(catRepository, times(1)).findAll();
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    @Transactional
    void testSaveCat_SuccessfulSave(){
        when(catRepository.save(any()))
                .thenReturn(cat1);
        CatInfo savedCat= catService.saveCat(catCommand1);
        catInfo1.setLastPlay(savedCat.getLastPlay());
        assertEquals(catInfo1, savedCat);
        verify(catRepository, times(1)).save(any());
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    @Transactional
    void testUpdateCat_SuccessfulUpdate(){
        when(catRepository.findById(any()))
                .thenReturn(Optional.of(catForUpdate));
        when(catRepository.update(any()))
                .thenReturn(catForUpdate);
        when(catRepository.save(any())).thenReturn(cat1);
        catService.saveCat(catCommand1);
        assertEquals("Luci", catService.updateCat(1, updateCommand1).getName());
        verify(catRepository, times(1)).save(any());
        verify(catRepository, times(1)).update(any());
        verify(catRepository, times(2)).findById(any());
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    @Transactional
    void testWhoNeedsToPlay() {
        String ldt = "2021-08-03 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        cat2.setLastPlay(dateTime);
        when(catRepository.findAll()).thenReturn(List.of(cat2));
        when(catRepository.save(any())).thenReturn(cat2);
        catCommand2.setLastPlay(dateTime);
        CatInfo savedCat = catService.saveCat(catCommand2);
        assertEquals(dateTime, savedCat.getLastPlay());
        assertEquals(1, catService.whoNeedsToPlay().size());
        assertEquals(1, catService.findAllCats().size());

        verify(catRepository, times(1)).save(any());
        verify(catRepository, times(2)).findAll();
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    @Transactional
    void testFindById_SuccessfulFind(){
        when(catRepository.findById(2)).thenReturn(Optional.of(cat2));
        when(catRepository.save(any())).thenReturn(cat2);
        CatInfo savedCat = catService.saveCat(catCommand2);
        assertEquals("Ribizli", catService.findById(2).getName());

        verify(catRepository, times(1)).save(any());
        verify(catRepository, times(1)).findById(any());
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    @Transactional
    void testFindById_CatNotFound(){
        when(catRepository.findById(3)).thenThrow(new CatNotFoundException());
        when(catRepository.findById(2)).thenReturn(Optional.of(cat2));
        when(catRepository.save(any())).thenReturn(cat2);
        catService.saveCat(catCommand2);
        assertEquals(catInfo2, catService.findById(2));
        assertThrows(CatNotFoundException.class, () -> {
            catService.findById(3);
        });
        verify(catRepository, times(1)).save(any());
        verify(catRepository, times(2)).findById(any());
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    @Transactional
    void testFindBestFriend_NoBestFriend(){
        when(catRepository.findById(any())).thenReturn(Optional.of(cat1));
        assertThrows(NoBestFriendException.class, () -> catService.findBestFriend(1), "Sadly, this animal has no best friend.");
        verify(catRepository, times(1)).findById(1);
        verifyNoMoreInteractions(catRepository);
    }

    @Test
    @Transactional
    void testFindBestFriend_SuccessfulFind(){
        when(catRepository.findById(any())).thenReturn(Optional.of(cat3));
        assertEquals(dogInfo1, catService.findBestFriend(3));

    }


}

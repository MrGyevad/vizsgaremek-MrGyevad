package hu.progmasters.animalShelter.unit;

import hu.progmasters.animalShelter.domain.*;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BestFriendRepositoryTest {

    @Autowired
    CatRepository catRepository;

    @Autowired
    DogRepository dogRepository;

    @Autowired
    BestFriendRepository bestFriendRepository;

    @Test
    @Order(1)
    @Transactional
    void testFindAll_EmptyList(){
        assertTrue(bestFriendRepository.findAll().isEmpty());
    }

    @Test
    @Order(2)
    @Transactional
    void testFindAll_TwoFriendshipsInList(){
        BestFriend bestFriend = new BestFriend();
        BestFriend bestFriend1 = new BestFriend();
        bestFriendRepository.save(bestFriend);
        bestFriendRepository.save(bestFriend1);
        assertEquals(2, bestFriendRepository.findAll().size());
    }


    @Test
    @Order(3)
    @Transactional
    void testBecomeBestFriends_SuccessfulSave() {
        assertTrue(bestFriendRepository.findAll().isEmpty());
        Cat cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        Dog dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        dogRepository.save(dog1);
        catRepository.save(cat1);
        BestFriend saved = new BestFriend();
        if (bestFriendRepository.findFriendshipById(1).isPresent()){
            saved = bestFriendRepository.findFriendshipById(1).get();
        }
        bestFriendRepository.becomeBestFriends(saved, cat1, dog1);
        assertEquals(1, bestFriendRepository.findAll().size());
        assertEquals(1, saved.getId());
        assertEquals("Lucifer", saved.getCat().getName());
        assertEquals("Sirion", saved.getDog().getName());
    }
}
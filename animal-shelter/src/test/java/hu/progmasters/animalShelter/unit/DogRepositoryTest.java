package hu.progmasters.animalShelter.unit;

import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.exception.DogNotFoundException;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DogRepositoryTest {

    @Autowired
    DogRepository dogRepository;

    @Autowired
    BestFriendRepository bestFriendRepository;

    @Test
    @Order(1)
    @Transactional
    void testSaveDog_successfulSave(){
        Dog toSave = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        LocalDateTime dateTime = toSave.getLastWalk();
        assertTrue(dogRepository.findAll().isEmpty());
        Dog saved = dogRepository.save(toSave);
        assertEquals(1, dogRepository.findAll().size());
        assertEquals(1, saved.getId());
        assertEquals(6, saved.getAge());
        assertTrue(saved.isHasWaterAndFood());
        assertFalse(saved.isAdopted());
        assertEquals("Sirion", saved.getName());
        assertEquals("Mudi", saved.getBreed());
        assertEquals(Gender.SIRE, saved.getGender());
        assertEquals(dateTime, saved.getLastWalk());
    }

    @Test
    @Order(2)
    @Transactional
    void testUpdateDog_successfulUpdate(){
        Dog dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        Dog dog2 = new Dog(1, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
        LocalDateTime dateTime = dog2.getLastWalk();
        dogRepository.save(dog1);
        Dog updated = dogRepository.update(dog2);
        assertEquals(1, dogRepository.findAll().size());
        assertEquals(1, updated.getId());
        assertEquals(11, dogRepository.findById(updated.getId()).get().getAge());
        assertTrue(updated.isHasWaterAndFood());
        assertFalse(updated.isAdopted());
        assertEquals("Diego", dogRepository.findById(1).get().getName());
        assertEquals("Maltese", updated.getBreed());
        assertEquals(Gender.SIRE, updated.getGender());
        assertEquals(dateTime, updated.getLastWalk());
    }

    @Test
    @Order(3)
    @Transactional
    void testFindById_successfulFind(){
        Dog dog2 = new Dog(1, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
        LocalDateTime dateTime = dog2.getLastWalk();
        assertTrue(dogRepository.findAll().isEmpty());
        dogRepository.save(dog2);
        Dog found = dogRepository.findById(1).get();
        assertEquals(1, dogRepository.findAll().size());
        assertEquals(1, found.getId());
        assertEquals(11, found.getAge());
        assertTrue(found.isHasWaterAndFood());
        assertFalse(found.isAdopted());
        assertEquals("Diego", found.getName());
        assertEquals("Maltese", found.getBreed());
        assertEquals(Gender.SIRE, found.getGender());
        assertEquals(dateTime, found.getLastWalk());
    }
    
    @Test
    @Order(4)
    @Transactional
    void testDelete_successfulDelete(){
        Dog toDelete = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        dogRepository.save(toDelete);
        assertEquals(1, dogRepository.findAll().size());
        dogRepository.dogDeceased(toDelete);
        assertEquals(0, dogRepository.findAll().size());
    }
    
    @Test
    @Order(5)
    @Transactional
    void testFindAll_allFound(){
        Dog dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        Dog dog2 = new Dog(2, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
        Dog dog3 = new Dog(3, "Réka", 15, "Vizsla", Gender.BITCH, LocalDateTime.now(), true, false, new BestFriend(3, null, null));
        dogRepository.save(dog1);
        assertEquals(1, dogRepository.findAll().size());
        dogRepository.save(dog2);
        assertEquals(2, dogRepository.findAll().size());
        dogRepository.save(dog3);
        assertEquals(3, dogRepository.findAll().size());
    }
    
    @Test
    @Order(6)
    @Transactional
    void testFindAllByGender_allFound(){
        Dog dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        Dog dog2 = new Dog(2, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
        Dog dog3 = new Dog(3, "Réka", 15, "Vizsla", Gender.BITCH, LocalDateTime.now(), true, false, new BestFriend(3, null, null));
        dogRepository.save(dog1);
        dogRepository.save(dog2);
        dogRepository.save(dog3);
        assertEquals(1, dogRepository.findAllByGender(Gender.BITCH).size());
        assertEquals(2, dogRepository.findAllByGender(Gender.SIRE).size());
    }
    
    @Test
    @Order(7)
    @Transactional
    void testWalkMeBoy_dogWalked(){
        Dog dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        dogRepository.save(dog1);
        LocalDateTime originalTime = dog1.getLastWalk();
        if (dogRepository.findById(1).isPresent()) {
            assertEquals(originalTime, dogRepository.findById(1).get().getLastWalk());
            dogRepository.walkMeBoy(1);
            assertNotEquals(originalTime, dogRepository.findById(1).get().getLastWalk());
        } else {
            throw new DogNotFoundException();
        }
    }

    /*
    String ldt = "2021-08-03 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        */
 }

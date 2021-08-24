package hu.progmasters.animalShelter.unit;

import hu.progmasters.animalShelter.domain.AnimalShelter;
import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
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
public class DogRepositoryTest {

    @Autowired
    DogRepository dogRepository;

    @Autowired
    BestFriendRepository bestFriendRepository;


    @Test
    @Order(1)
    @Transactional
    void testSaveDog_successfulSave() {
        Dog toSave = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
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
    void testUpdateDog_successfulUpdate() {
        Dog dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        Dog dog2 = new Dog(1, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null), new AnimalShelter());
        LocalDateTime dateTime = dog2.getLastWalk();
        dogRepository.save(dog1);
        Dog updated = dogRepository.update(dog2);
        assertEquals(1, dogRepository.findAll().size());
        assertEquals(1, updated.getId());
        assertEquals(11, updated.getAge());
        assertTrue(updated.isHasWaterAndFood());
        assertFalse(updated.isAdopted());
        assertEquals("Diego", updated.getName());
        assertEquals("Maltese", updated.getBreed());
        assertEquals(Gender.SIRE, updated.getGender());
        assertEquals(dateTime, updated.getLastWalk());
    }

    @Test
    @Order(3)
    @Transactional
    void testFindById_successfulFind() {
        Dog dog2 = new Dog(1, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null), new AnimalShelter());
        LocalDateTime dateTime = dog2.getLastWalk();
        assertTrue(dogRepository.findAll().isEmpty());
        dogRepository.save(dog2);
        Dog found = new Dog();
        if (dogRepository.findById(1).isPresent()) {
            found = dogRepository.findById(1).get();
        }
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
    void testDelete_successfulDelete() {
        Dog toDelete = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        dogRepository.save(toDelete);
        assertEquals(1, dogRepository.findAll().size());
        dogRepository.dogDeceased(toDelete);
        assertEquals(0, dogRepository.findAll().size());
    }

    @Test
    @Order(5)
    @Transactional
    void testFindAll_allFound() {
        Dog dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        Dog dog2 = new Dog(2, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null), new AnimalShelter());
        Dog dog3 = new Dog(3, "Réka", 15, "Vizsla", Gender.BITCH, LocalDateTime.now(), true, false, new BestFriend(3, null, null), new AnimalShelter());
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
    void testFindAllByGender_allFound() {
        Dog dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        Dog dog2 = new Dog(2, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(2, null, null), new AnimalShelter());
        Dog dog3 = new Dog(3, "Réka", 15, "Vizsla", Gender.BITCH, LocalDateTime.now(), true, false, new BestFriend(3, null, null), new AnimalShelter());
        dogRepository.save(dog1);
        dogRepository.save(dog2);
        dogRepository.save(dog3);
        assertEquals(1, dogRepository.findAllByGender(Gender.BITCH).size());
        assertEquals(2, dogRepository.findAllByGender(Gender.SIRE).size());
    }
}

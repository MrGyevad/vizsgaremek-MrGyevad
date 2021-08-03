package hu.progmasters.animalShelter;

import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.repository.DogRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DogRepositoryTest {

    @Autowired
    DogRepository dogRepository;

    @Test
    @Order(1)
    @Transactional
    void testSaveDog_successfulSave(){
        String ldt = "2021-08-03 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        Dog toSave = new Dog();
        toSave.setLastWalk(dateTime);
        toSave.setHasWaterAndFood(true);
        toSave.setGender(Gender.SIRE);
        toSave.setName("Sirion");
        toSave.setBreed("Moody");
        toSave.setAge(6);
        toSave.setGoneStray(false);
        assertTrue(dogRepository.findAll().isEmpty());
        Dog saved = dogRepository.save(toSave);
        assertEquals(1, dogRepository.findAll().size());
        assertEquals(1, saved.getId());
        assertEquals(6, saved.getAge());
        assertTrue(saved.isHasWaterAndFood());
        assertFalse(saved.isGoneStray());
        assertEquals("Sirion", saved.getName());
        assertEquals("Moody", saved.getBreed());
        assertEquals(Gender.SIRE, saved.getGender());
        assertEquals(dateTime, saved.getLastWalk());
    }

    @Test
    @Order(2)
    @Transactional
    void testUpdateDog_successfulUpdate(){
        String ldt = "2021-08-03 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        Dog toUpdate = new Dog();
        toUpdate.setId(1);
        toUpdate.setLastWalk(dateTime);
        toUpdate.setHasWaterAndFood(true);
        toUpdate.setGender(Gender.SIRE);
        toUpdate.setName("Diego");
        toUpdate.setBreed("Maltese");
        toUpdate.setAge(11);
        toUpdate.setGoneStray(false);
        Dog updated = dogRepository.update(toUpdate);
        assertEquals(1, dogRepository.findAll().size());
        assertEquals(1, updated.getId());
        assertEquals(11, dogRepository.findById(updated.getId()).getAge());
        assertTrue(updated.isHasWaterAndFood());
        assertFalse(updated.isGoneStray());
        assertEquals("Diego", dogRepository.findById(1).getName());
        assertEquals("Maltese", updated.getBreed());
        assertEquals(Gender.SIRE, updated.getGender());
        assertEquals(dateTime, updated.getLastWalk());
    }







}

package hu.progmasters.animalShelter.unit;

import hu.progmasters.animalShelter.domain.AnimalShelter;
import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.repository.CatRepository;
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
public class CatRepositoryTest {

    @Autowired
    CatRepository catRepository;

    @Test
    @Order(1)
    @Transactional
    void testSaveCat_successfulSave(){
        Cat cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        assertTrue(catRepository.findAll().isEmpty());
        Cat saved = catRepository.save(cat1);
        assertEquals(1, catRepository.findAll().size());
        assertEquals(1, cat1.getId());
        assertEquals(10, cat1.getAge());
        assertTrue(cat1.isHasWaterAndFood());
        assertFalse(cat1.isAdopted());
        assertEquals("Lucifer", cat1.getName());
        assertEquals("Giant", cat1.getBreed());
        assertEquals(Gender.TOM, saved.getGender());
    }

    @Test
    @Order(2)
    @Transactional
    void testUpdateCat_successfulUpdate(){
        Cat cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        Cat cat2 = new Cat(1, "Ribizli", 5, "Halfear", Gender.PUSSY, LocalDateTime.now(), true, false, new BestFriend(2, null, null), new AnimalShelter());
        LocalDateTime cat2LastPlay = cat2.getLastPlay();
        catRepository.save(cat1);
        Cat updated = catRepository.update(cat2);
        assertEquals(1, catRepository.findAll().size());
        assertEquals(1, updated.getId());
        assertEquals(5, updated.getAge());
        assertTrue(updated.isHasWaterAndFood());
        assertFalse(updated.isAdopted());
        assertEquals("Ribizli", updated.getName());
        assertEquals("Halfear", updated.getBreed());
        assertEquals(Gender.PUSSY, updated.getGender());
        assertEquals(cat2LastPlay, updated.getLastPlay());
    }

    @Test
    @Order(3)
    @Transactional
    void testFindById_successfulFind(){
        Cat cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        LocalDateTime cat1LastPlay = cat1.getLastPlay();
        assertTrue(catRepository.findAll().isEmpty());
        catRepository.save(cat1);
        Cat found = new Cat();
        if (catRepository.findById(1).isPresent()){
        found = catRepository.findById(1).get();
        }
        assertEquals(1, catRepository.findAll().size());
        assertEquals(1, found.getId());
        assertEquals(10, found.getAge());
        assertTrue(found.isHasWaterAndFood());
        assertFalse(found.isAdopted());
        assertEquals("Lucifer", found.getName());
        assertEquals("Giant", found.getBreed());
        assertEquals(Gender.TOM, found.getGender());
        assertEquals(cat1LastPlay, found.getLastPlay());
    }
    
    @Test
    @Order(4)
    @Transactional
    void testDelete_successfulDelete(){
        Cat cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        catRepository.save(cat1);
        assertEquals(1, catRepository.findAll().size());
        catRepository.catDeceased(cat1);
        assertEquals(0, catRepository.findAll().size());
    }
    
    @Test
    @Order(5)
    @Transactional
    void testFindAll_allFound(){
        Cat cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        Cat cat2 = new Cat(2, "Ribizli", 5, "Halfear", Gender.PUSSY, LocalDateTime.now(), true, false, new BestFriend(2, null, null), new AnimalShelter());
        Cat cat3 = new Cat(3, "Nudli", 0, "Mix", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(3, null, null), new AnimalShelter());
        catRepository.save(cat1);
        assertEquals(1, catRepository.findAll().size());
        catRepository.save(cat2);
        assertEquals(2, catRepository.findAll().size());
        catRepository.save(cat3);
        assertEquals(3, catRepository.findAll().size());
    }
    
    @Test
    @Order(6)
    @Transactional
    void testFindAllByGender_allFound(){
        Cat cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null), new AnimalShelter());
        Cat cat2 = new Cat(2, "Ribizli", 5, "Halfear", Gender.PUSSY, LocalDateTime.now(), true, false, new BestFriend(2, null, null), new AnimalShelter());
        Cat cat3 = new Cat(3, "Nudli", 0, "Mix", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(3, null, null), new AnimalShelter());
        catRepository.save(cat1);
        catRepository.save(cat2);
        catRepository.save(cat3);
        assertEquals(1, catRepository.findAllByGender(Gender.PUSSY).size());
        assertEquals(2, catRepository.findAllByGender(Gender.TOM).size());
    }
 }

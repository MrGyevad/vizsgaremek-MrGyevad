package hu.progmasters.animalShelter;

import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.repository.CatRepository;
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
public class CatRepositoryTest {

    @Autowired
    CatRepository catRepository;

    @Test
    @Order(1)
    @Transactional
    void testSaveDog_successfulSave(){      
        cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        assertTrue(catRepository.findAll().isEmpty());
        Cat saved = catRepository.save(cat1);
        assertEquals(1, catRepository.findAll().size());
        assertEquals(1, cat1.getId());
        assertEquals(6, cat1.getAge());
        assertTrue(cat1.isHasWaterAndFood());
        assertFalse(cat1.isAdopted());
        assertEquals("Lucifer", cat1.getName());
        assertEquals("Giant", cat1.getBreed());
        assertEquals(Gender.TOM, saved.getGender());
    }

    @Test
    @Order(2)
    @Transactional
    void testUpdateDog_successfulUpdate(){
        cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        cat2 = new Cat(1, "Ribizli", 5, "Halfear", Gender.PUSSY, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
        LocalDateTime cat2LastPlay = cat2.getLastPlay();
        catRepository.save(cat1);
        Cat updated = catRepository.update(cat2);
        assertEquals(1, catRepository.findAll().size());
        assertEquals(1, updated.getId());
        assertEquals(4, updated.getAge());
        assertTrue(updated.isHasWaterAndFood());
        assertFalse(updated.isAdopted());
        assertEquals("Retek", updated.getName());
        assertEquals("Fungusnail", updated.getBreed());
        assertEquals(Gender.SIRE, updated.getGender());
        assertEquals(cat2LastWalk, updated.getLastPlay());
    }

    @Test
    @Order(3)
    @Transactional
    void testFindById_successfulFind(){
        cat1 = toSave Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        LocalDateTime cat1LastPlay = cat1.getLastPlay();
        assertTrue(catRepository.findAll().isEmpty());
        catRepository.save(toSave);
        Cat found = catRepository.findById(1);
        assertEquals(1, catRepository.findAll().size());
        assertEquals(1, found.getId());
        assertEquals(10, found.getAge());
        assertTrue(found.isHasWaterAndFood());
        assertFalse(found.isAdopted());
        assertEquals("Lucifer", found.getName());
        assertEquals("Giant", found.getBreed());
        assertEquals(Gender.TOM, found.getGender());
        assertEquals(cat1LastPlay(), found.getLastPlay());
    }
    
    @Test
    @Order(4)
    @Transactional
    void testDelete_successfulDelete(){
        cat1 = toSave Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        catRepository.save(toDelete);
        assertEquals(1, catRepository.findAll().size());
        catRepository.delete(toDelete);
        assertEquals(0, catRepository.findAll().size());
    }
    
    @Test
    @Order(5)
    @Transactional
    void testFindAll_allFound(){
        cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        cat2 = new Cat(2, "Ribizli", 5, "Halfear", Gender.PUSSY, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
        cat3 = new Cat(3, "Nudli", 0, "Mix", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(3, null, null));
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
        cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        cat2 = new Cat(2, "Ribizli", 5, "Halfear", Gender.PUSSY, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
        cat3 = new Cat(3, "Nudli", 0, "Mix", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(3, null, null));
        catRepository.save(cat1);
        catRepository.save(cat2);
        catRepository.save(cat3);
        assertEquals(1, catRepository.findAllByGender(Gender.PUSSY).size());
        assertEquals(2, catRepository.findAllByGender(Gender.TOM).size());
    }
    
    @Test
    @Order(7)
    @Transactional
    void testPlayWithMeGirl_playedWithCat(){
        cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
        catRepository.save(cat1);
        LocalDateTime originalTime = cat1.getLastPlay();
        assertEquals(originalTime, catRepository.findById(1).getLastPlay());
        catRepository.playWithMeGirl(1);
        assertNotEquals(originalTime, catRepository.findById(1).getLastPlay());
    }
 }

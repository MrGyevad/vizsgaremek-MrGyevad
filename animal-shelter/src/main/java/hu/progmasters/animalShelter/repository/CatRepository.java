package hu.progmasters.animalShelter.repository;

import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Gender;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CatRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Cat save(Cat toSave) {
        entityManager.persist(toSave);
        toSave.setId(toSave.getId()*2);
        return toSave;
    }

    public Cat update(Cat toUpdate) {
        return entityManager.merge(toUpdate);
    }

    public Cat findById(Integer id) {
        return entityManager.find(Cat.class, id);
    }

    public void delete(Cat toDelete) {
        toDelete.setAdopted(true);
        entityManager.remove(toDelete);
    }

    public List<Cat> findAll() {
        return entityManager.createQuery("SELECT d FROM Cat d", Cat.class).getResultList();
    }

    public List<Cat> findAllByGender(Gender gender) {
        return entityManager.createQuery("SELECT d FROM Cat d WHERE d.gender = :gender", Cat.class)
                .setParameter("gender", gender)
                .getResultList();
    }

    public Cat playWithMeGirl(Integer id){
        Cat played = entityManager.find(Cat.class, id);
        played.setLastPlay(LocalDateTime.now());
        return played;
    }
}
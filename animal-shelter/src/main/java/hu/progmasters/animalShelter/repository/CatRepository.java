package hu.progmasters.animalShelter.repository;

import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Gender;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class CatRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Cat save(Cat toSave) {
        return entityManager.merge(toSave);
    }

    public Cat update(Cat toUpdate) {
        return entityManager.merge(toUpdate);
    }

    public Optional<Cat> findById(Integer id) {
        if (entityManager.find(Cat.class, id) != null){
            return Optional.of(entityManager.find(Cat.class, id));
        } else {
            return Optional.empty();
        }
    }

    public void catDeceased(Cat toDelete){
        entityManager.remove(entityManager.contains(toDelete) ? toDelete : entityManager.merge(toDelete));
    }

    public List<Cat> findAll() {
        return entityManager.createQuery("SELECT c FROM Cat c WHERE c.adopted IN :value ", Cat.class)
                .setParameter("value", false)
                .getResultList();
    }

    public List<Cat> findAllByGender(Gender gender) {
        return entityManager.createQuery("SELECT c FROM Cat c WHERE c.gender = :gender", Cat.class)
                .setParameter("gender", gender)
                .getResultList();
    }

    public Cat playWithMeGirl(Integer id) throws InterruptedException {
        Optional<Cat> catOptional = findById(id);
        if (catOptional.isPresent()) {
            Cat played = catOptional.get();
            TimeUnit.MILLISECONDS.sleep(1);
            played.setLastPlay(LocalDateTime.now());
            return entityManager.merge(played);
        } else {
            return null;
        }
    }
    public List<Cat> findAllAdopted() {
        return entityManager.createQuery("SELECT c FROM Cat c WHERE c.adopted IN :value", Cat.class)
                .setParameter("value", true)
                .getResultList();
    }
}
package hu.progmasters.animalShelter.repository;

import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Gender;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        return Optional.of(entityManager.find(Cat.class, id));
    }

    public void delete(Optional<Cat> toDelete) {
        toDelete.ifPresent(cat -> cat.setAdopted(true));
        toDelete.ifPresent(cat -> entityManager.remove(entityManager.contains(cat) ? cat : entityManager.merge(cat)));
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

    public Cat playWithMeGirl(Integer id){
        Cat played = entityManager.find(Cat.class, id);
        played.setLastPlay(LocalDateTime.now());
        return played;
    }
}
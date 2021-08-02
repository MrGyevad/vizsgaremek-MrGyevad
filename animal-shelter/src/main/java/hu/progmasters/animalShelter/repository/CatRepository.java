package hu.progmasters.animalShelter.repository;

import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Gender;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        //TODO StrayRepository-t megírni, a törölt kutyáknak, egy másik táblázat.
        toDelete.setGoneStray(true);
        // StrayService.save(toDelete);
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
}
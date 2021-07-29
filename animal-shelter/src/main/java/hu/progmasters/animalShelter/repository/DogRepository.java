package hu.progmasters.animalShelter.repository;


import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DogRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Dog save(Dog toSave){
        entityManager.persist(toSave);
        return toSave;
    }

    public Dog update(Dog toUpdate){
        Dog updated = entityManager.merge(toUpdate);
        return updated;
    }

    public Dog findById(Integer id){
        return entityManager.find(Dog.class, id);
    }

    public void delete(Dog toDelete){
        //TODO StrayRepository-t megírni, a törölt kutyáknak, egy másik táblázat.
        toDelete.setGoneStray(true);
        // StrayService.save(toDelete);
        entityManager.remove(toDelete);
    }

    public List<Dog> findAll(){
        return entityManager.createQuery("SELECT d FROM Dog d", Dog.class).getResultList();
    }

    public List<Dog> findAllByGender(Gender gender){
        return entityManager.createQuery("SELECT d FROM Dog d WHERE d.gender = :gender", Dog.class)
                .setParameter("gender", gender)
                .getResultList();
    }

}

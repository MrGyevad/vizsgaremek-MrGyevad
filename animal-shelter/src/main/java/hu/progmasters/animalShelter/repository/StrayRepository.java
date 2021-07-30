package hu.progmasters.animalShelter.repository;

import hu.progmasters.animalShelter.domain.Animal;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class StrayRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Animal saveGoneStray(Animal animal){
        entityManager.persist(animal);
        return animal;
    }

    public Animal hasBeenFound(Animal animal){
        entityManager.remove(animal);
        return animal;
    }

    public Animal findById(Integer id){
        return entityManager.find(Animal.class, id);
    }
}

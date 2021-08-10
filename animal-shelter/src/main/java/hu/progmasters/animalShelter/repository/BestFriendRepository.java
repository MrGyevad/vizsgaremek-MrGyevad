package hu.progmasters.animalShelter.repository;

import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class BestFriendRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveAdoptedCat(Cat cat, Dog dog) {
        entityManager.persist(dog);
        entityManager.persist(cat);
    }

    public void saveAdoptedDog(Dog dog, Cat cat) {
        entityManager.persist(dog);
        entityManager.persist(cat);
    }


}

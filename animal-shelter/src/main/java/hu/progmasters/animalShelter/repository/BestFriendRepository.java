package hu.progmasters.animalShelter.repository;

import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class BestFriendRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public BestFriend save(BestFriend toSave) {
        return entityManager.merge(toSave);
    }

    public BestFriend becomeBestFriends(BestFriend bestFriend, Cat cat, Dog dog) {
        cat.setBestFriend(bestFriend);
        bestFriend.setCat(cat);
        dog.setBestFriend(bestFriend);
        bestFriend.setDog(dog);
        return entityManager.merge(bestFriend);
    }

    public Optional<BestFriend> findFriendshipById(Integer id) {

        if (entityManager.find(BestFriend.class, id) != null) {
            return Optional.of(entityManager.find(BestFriend.class, id));
        } else return Optional.empty();
    }

    public List<BestFriend> findAll() {
        return entityManager.createQuery("SELECT b FROM BestFriend b", BestFriend.class).getResultList();
    }
}

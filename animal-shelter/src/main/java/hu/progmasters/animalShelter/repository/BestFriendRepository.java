package hu.progmasters.animalShelter.repository;

import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BestFriendRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public BestFriend save(BestFriend toSave){
        entityManager.persist(toSave);
        return toSave;
    }
    
    public BestFriend becomeBestFriends(BestFriend bestFriend, Cat cat, Dog dog){
        cat.setBestFriend(bestFriend);
        bestFriend.setCat(cat);
        dog.setBestFriend(bestFriend);
        bestFriend.setDog(dog);
        return entityManager.merge(bestFriend);
    }
    
    public BestFriend findFriendshipById(Integer id){
        return entityManager.find(BestFriend.class, id);
    }
    
    public List<BestFriend> findAll(){
        return entityManager.createQuery("SELECT b FROM BestFriend b", BestFriend.class).getResultList();
    }


}

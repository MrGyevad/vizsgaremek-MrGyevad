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
    
    private Cat cat;
    private Dog dog;
    
    public BestFriend becomeBestFriends(Cat cat, Dog dog){
        BestFriend toSave = new BestFriend(Cat cat, Dog dog);
        BestFriend saved = entityManager.persist(toSave);
        cat.setBestFriendId(saved.getId());
        dog.setBestFriendId(saved.getId());
        return saved;
    }
    
    public BestFriend findFriendshipById(Integer id){
        return entityManager.find(BestFriend.class, id);
    }

    public void saveAdoptedCat(Cat cat) {
        BestFriend friendship = findFriendshipById(cat.getBestFriendId());
        
    }

    public void saveAdoptedDog(Dog dog, Cat cat) {
        entityManager.persist(dog);
        entityManager.persist(cat);
    }
    
    public List<BestFriend> findAll(){
        return entityManager.createQuery("SELECT b FROM BestFriend b", BestFriend.class).getResultList();
    }


}

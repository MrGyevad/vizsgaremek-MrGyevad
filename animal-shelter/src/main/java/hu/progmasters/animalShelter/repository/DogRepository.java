package hu.progmasters.animalShelter.repository;


import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
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
        return entityManager.merge(toUpdate);
    }

    public Dog findById(Integer id){
        return entityManager.find(Dog.class, id);
    }

    public void delete(Dog toDelete){
        toDelete.setAdopted(true);
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

    public Dog walkMeBoy(Integer id){
        Dog walked = entityManager.find(Dog.class, id);
        walked.setLastWalk(LocalDateTime.now());
        return walked;
    }
}

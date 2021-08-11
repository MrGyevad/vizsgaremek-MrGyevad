package hu.progmasters.animalShelter.repository;


import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DogRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Dog save(Dog toSave){
        return entityManager.merge(toSave);

    }

    public Dog update(Dog toUpdate){
        return entityManager.merge(toUpdate);
    }

    public Optional<Dog> findById(Integer id){
        return Optional.of(entityManager.find(Dog.class, id));
    }

    public void delete(Optional<Dog> toDelete){
        toDelete.ifPresent(dog -> dog.setAdopted(true));
        toDelete.ifPresent(dog -> entityManager.remove(entityManager.contains(dog) ? dog : entityManager.merge(dog)));
    }

    public List<Dog> findAll(){
        return entityManager.createQuery("SELECT d FROM Dog d WHERE d.adopted IN :value", Dog.class)
                .setParameter("value", false)
                .getResultList();
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

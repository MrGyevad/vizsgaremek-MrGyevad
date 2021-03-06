package hu.progmasters.animalShelter.repository;


import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        if (entityManager.find(Dog.class, id) != null){
            return Optional.of(entityManager.find(Dog.class, id));
        } else return Optional.empty();
    }

    public void dogDeceased(Dog toDelete){
        entityManager.remove(entityManager.contains(toDelete) ? toDelete : entityManager.merge(toDelete));
    }

    public List<Dog> findAll(){
        return entityManager.createQuery("SELECT d FROM Dog d WHERE d.adopted IN :value", Dog.class)
                .setParameter("value", false)
                .getResultList();
    }

    public List<Dog> findAllByGender(Gender gender){
        return entityManager.createQuery("SELECT d FROM Dog d WHERE d.gender IN :gender", Dog.class)
                .setParameter("gender", gender)
                .getResultList();
    }

    public List<Dog> findAllAdopted(){
        return entityManager.createQuery("SELECT d FROM Dog d WHERE d.adopted IN :value", Dog.class)
                .setParameter("value", true)
                .getResultList();
    }
}

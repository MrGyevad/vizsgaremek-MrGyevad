package hu.progmasters.animalShelter.repository;

import hu.progmasters.animalShelter.domain.AnimalShelter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimalShelterRepository {

    @PersistenceContext
    EntityManager entityManager;

    public AnimalShelter save(AnimalShelter toSave) {
        entityManager.persist(toSave);
        return toSave;
    }

    public AnimalShelter update(AnimalShelter toUpdate) {
        return entityManager.merge(toUpdate);
    }

    public List<AnimalShelter> findAll(){
        return entityManager.createQuery("SELECT a FROM AnimalShelter a", AnimalShelter.class)
                .getResultList();
    }

    public void delete(AnimalShelter toDelete){
        entityManager.remove(entityManager.contains(toDelete) ? toDelete : entityManager.merge(toDelete));
    }

    public Optional<AnimalShelter> findById(Integer id){
        return Optional.of(entityManager.find(AnimalShelter.class, id));
    }
}

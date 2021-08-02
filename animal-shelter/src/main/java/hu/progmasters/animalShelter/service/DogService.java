package hu.progmasters.animalShelter.service;

import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.DogCommand;
import hu.progmasters.animalShelter.dto.DogInfo;
import hu.progmasters.animalShelter.exception.DogNotFoundException;
import hu.progmasters.animalShelter.repository.DogRepository;
import hu.progmasters.animalShelter.repository.StrayRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DogService {

    private final DogRepository dogRepository;
    private final StrayRepository strayRepository;
    private final ModelMapper modelMapper;

    public DogService(DogRepository dogRepository, StrayRepository strayRepository, ModelMapper modelMapper) {
        this.dogRepository = dogRepository;
        this.strayRepository = strayRepository;
        this.modelMapper = modelMapper;
    }

    public DogInfo saveDog(DogCommand command){
        Dog toSave = modelMapper.map(command, Dog.class);
        Dog saved = dogRepository.save(toSave);
        return modelMapper.map(saved, DogInfo.class);
    }

    public DogInfo updateDog(Integer id, DogCommand command){
        Dog toUpdate = dogRepository.findById(id);
        toUpdate.setAge(command.getAge());
        toUpdate.setBreed(command.getBreed());
        toUpdate.setName(command.getName());
        toUpdate.setGoneStray(command.isGoneStray());
        toUpdate.setGender(command.getGender());
        Dog updated = dogRepository.update(toUpdate);
        return modelMapper.map(updated, DogInfo.class);
    }

    public List<DogInfo> findAllDogs(){
        List<Dog> dogs = dogRepository.findAll();
        return dogs.stream().map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
    }

    public DogInfo findById(Integer id){
        Dog found = dogRepository.findById(id);
        if (found != null){
            return modelMapper.map(found, DogInfo.class);
        } else {
            throw new DogNotFoundException();
        }
    }

    public List<DogInfo> findAllByGender(Gender gender){
        List<Dog> foundByGender = dogRepository.findAllByGender(gender);
        return foundByGender.stream().map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
    }

    public void dogGoneStray(Integer id){
        Dog toDelete = dogRepository.findById(id);
        toDelete.setGoneStray(true);
        strayRepository.saveGoneStray(toDelete);
        dogRepository.delete(toDelete);
    }

    public DogInfo dogHasBeenFound(Integer id){
        Dog found = (Dog) strayRepository.findById(id);
        Dog isAtHome = dogRepository.save(found);
        Dog hasBeenFound = (Dog) strayRepository.hasBeenFound(isAtHome);
        return modelMapper.map(isAtHome, DogInfo.class);
    }
}

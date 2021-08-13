package hu.progmasters.animalShelter.service;

import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.CatInfo;
import hu.progmasters.animalShelter.dto.DogCommand;
import hu.progmasters.animalShelter.dto.DogInfo;
import hu.progmasters.animalShelter.exception.CatNotFoundException;
import hu.progmasters.animalShelter.exception.DogNotFoundException;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DogService {

    private final DogRepository dogRepository;
    private final CatRepository catRepository;
    private final BestFriendRepository bestFriendRepository;
    private final ModelMapper modelMapper;

    public DogService(DogRepository dogRepository, CatRepository catRepository, BestFriendRepository bestFriendRepository, ModelMapper modelMapper) {
        this.dogRepository = dogRepository;
        this.catRepository = catRepository;
        this.bestFriendRepository = bestFriendRepository;
        this.modelMapper = modelMapper;
    }

    public DogInfo saveDog(DogCommand command) {
        Dog toSave = modelMapper.map(command, Dog.class);
        Dog saved = dogRepository.save(toSave);
        return modelMapper.map(saved, DogInfo.class);
    }

    public DogInfo updateDog(Integer id, DogCommand command) {
        Dog dogToUpdate;
        if (dogRepository.findById(id).isPresent()) {
            dogToUpdate = dogRepository.findById(id).get();
            dogToUpdate.setAge(command.getAge());
            dogToUpdate.setBreed(command.getBreed());
            dogToUpdate.setName(command.getName());
            dogToUpdate.setAdopted(command.isAdopted());
            dogToUpdate.setGender(command.getGender());
            dogToUpdate.setHasWaterAndFood(command.isHasWaterAndFood());
        } else {
            throw new DogNotFoundException();
        }
        Dog updated = dogRepository.update(dogToUpdate);
        return modelMapper.map(updated, DogInfo.class);
    }

    public List<DogInfo> findAllDogs() {
        return dogRepository.findAll()
                .stream()
                .map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
    }

    public DogInfo findById(Integer id) {
        Optional<Dog> found = dogRepository.findById(id);
        if (found.isPresent()){
            return modelMapper.map(found.get(), DogInfo.class);
        } else {
            throw new DogNotFoundException();
        }
    }

    public List<DogInfo> findAllByGender(Gender gender) {
        List<Dog> foundByGender = dogRepository.findAllByGender(gender);
        return foundByGender.stream().map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
    }

    public void dogAdopted(Integer id) {
        Dog toAdopt = dogRepository.findById(id).get();
        Cat bestFriend;
        try {
            bestFriend = toAdopt.getBestFriend().getCat();
            bestFriend.setAdopted(true);
        } catch (CatNotFoundException e) {
            e.printStackTrace();
        }
        toAdopt.setAdopted(true);
    }

    public void dogDeceased(Integer id) {
        Optional<Dog> toDelete = dogRepository.findById(id);
        dogRepository.delete(toDelete);
    }

    public List<DogInfo> whoNeedsAWalk() {
        List<Dog> allDogs = dogRepository.findAll();
        List<Dog> needWalk = new ArrayList<>();
        for (Dog dog : allDogs) {
            long hours = ChronoUnit.HOURS.between(dog.getLastWalk(), LocalDateTime.now());
            if (hours > 6) {
                needWalk.add(dog);
            }
        }
        return needWalk.stream().map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
    }

    public DogInfo walkTheDog(Integer id) {
        Dog dog = dogRepository.walkMeBoy(id);
        return modelMapper.map(dog, DogInfo.class);
    }


    public void walkAllDogs() {
        for (Dog dog : dogRepository.findAll()) {
            long hours = ChronoUnit.HOURS.between(dog.getLastWalk(), LocalDateTime.now());
            if (hours > 6) {
                dog.setLastWalk(LocalDateTime.now());
            }
        }
    }

    public CatInfo findBestFriend(Integer id) {
        Optional<Dog> found = dogRepository.findById(id);
        if (found.isPresent()){
            return modelMapper.map(found.get().getBestFriend().getCat(), CatInfo.class);
        } else throw new DogNotFoundException();

    }
}

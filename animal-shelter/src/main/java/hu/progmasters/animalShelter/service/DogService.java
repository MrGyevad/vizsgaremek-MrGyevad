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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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

    public DogInfo saveDog(DogCommand command){
        command.setLastWalk(LocalDateTime.now());
        Dog toSave = modelMapper.map(command, Dog.class);
        Dog saved = dogRepository.save(toSave);
        return modelMapper.map(saved, DogInfo.class);
    }

    public DogInfo updateDog(Integer id, DogCommand command){
        Dog toUpdate = dogRepository.findById(id);
        if (toUpdate != null) {
            toUpdate.setAge(command.getAge());
            toUpdate.setBreed(command.getBreed());
            toUpdate.setName(command.getName());
            toUpdate.setAdopted(command.isAdopted());
            toUpdate.setGender(command.getGender());
            toUpdate.setHasWaterAndFood(command.isHasWaterAndFood());
        } else {
            throw new DogNotFoundException();
        }
        Dog updated = dogRepository.update(toUpdate);
        return modelMapper.map(updated, DogInfo.class);
    }

    public List<DogInfo> findAllDogs(){
        return dogRepository.createQuery("SELECT d FROM Dog d WHERE d.isAdopted LIKE :value, Dog.class)
                                         .setParameter("value", false)
                                         .getResultList()
                                         .stream()
                                         .map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
        /*
        List<Dog> dogs = dogRepository.findAll();
        return dogs.stream().map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
    */
    }

    public DogInfo findById(Integer id){
        DogInfo found = new DogInfo();
        try {
            found = modelMapper.map(dogRepository.findById(id), DogInfo.class);
        } catch (DogNotFoundException e){
            e.printStackTrace();
        }
        return found;
    }

    public List<DogInfo> findAllByGender(Gender gender){
        List<Dog> foundByGender = dogRepository.findAllByGender(gender);
        return foundByGender.stream().map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
    }

    public void dogAdopted(Integer id){
        Dog toAdopt = dogRepository.findById(id);
        Cat bestFriend = new Cat();
        try {
            bestFriend = toAdopt.getBestFriendId().getCat();
        bestFriend.setAdopted(true);
        catRepository.delete(bestFriend);
        } catch (CatNotFoundException e){
            e.printStackTrace();
        }
        toAdopt.setAdopted(true);
        bestFriendRepository.saveAdoptedDog(toAdopt, bestFriend);
        dogRepository.delete(toAdopt);
    }

    public void dogDeceased(Integer id){
        Dog toDelete = dogRepository.findById(id);
        dogRepository.delete(toDelete);
    }

    public List<DogInfo> whoNeedsAWalk(){
        List<Dog> allDogs = dogRepository.findAll();
        List<Dog> needWalk = new ArrayList<>();
        for (Dog dog : allDogs) {
            long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), dog.getLastWalk());
            if (hours > 6){
                needWalk.add(dog);
            }
        }
        return needWalk.stream().map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
    }

    public DogInfo walkTheDog(Integer id){
        Dog dog = dogRepository.walkMeBoy(id);
        return modelMapper.map(dog, DogInfo.class);
    }


    public void walkAllDogs() {
        for (Dog dog : dogRepository.findAll()) {
            long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), dog.getLastWalk());
            if (hours > 6){
                dog.setLastWalk(LocalDateTime.now());
            }
        }
    }

    public CatInfo findBestFriend(Integer id){
        CatInfo bestFriend = new CatInfo();
        try {
            bestFriend = modelMapper.map(dogRepository.findById(id).getBestFriendId().getCat(), CatInfo.class);
        } catch (CatNotFoundException e){
            e.printStackTrace();
        }
        return bestFriend;
    }
}

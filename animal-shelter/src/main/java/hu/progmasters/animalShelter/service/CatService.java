package hu.progmasters.animalShelter.service;

import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.CatCommand;
import hu.progmasters.animalShelter.dto.CatInfo;
import hu.progmasters.animalShelter.dto.DogInfo;
import hu.progmasters.animalShelter.exception.CatNotFoundException;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
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
public class CatService {

    private final CatRepository catRepository;
    private final DogRepository dogRepository;
    private final BestFriendRepository bestFriendRepository;
    private final ModelMapper modelMapper;

    public CatService(CatRepository catRepository, DogRepository dogRepository, BestFriendRepository bestFriendRepository, ModelMapper modelMapper) {
        this.catRepository = catRepository;
        this.dogRepository = dogRepository;
        this.bestFriendRepository = bestFriendRepository;
        this.modelMapper = modelMapper;
    }

    public CatInfo saveCat(CatCommand command){
        Cat toSave = modelMapper.map(command, Cat.class);
        Cat saved = catRepository.save(toSave);
        return modelMapper.map(saved, CatInfo.class);
    }

    public CatInfo updateCat(Integer id, CatCommand command){
        Cat toUpdate = catRepository.findById(id);
        if (toUpdate != null) {
            toUpdate.setAge(command.getAge());
            toUpdate.setBreed(command.getBreed());
            toUpdate.setName(command.getName());
            toUpdate.setAdopted(command.isAdopted());
            toUpdate.setGender(command.getGender());
            toUpdate.setHasWaterAndFood(command.isHasWaterAndFood());
            toUpdate.setLastPlay(command.getLastPlay());
        } else {
            throw new CatNotFoundException();
        }
        Cat updated = catRepository.update(toUpdate);
        return modelMapper.map(updated, CatInfo.class);
    }

    public List<CatInfo> findAllCats(){
        List<Cat> cats = catRepository.findAll();
        return cats.stream().map(cat -> modelMapper.map(cat, CatInfo.class)).collect(Collectors.toList());
    }

    public CatInfo findById(Integer id){
        Cat found = catRepository.findById(id);
        if (found != null){
            return modelMapper.map(found, CatInfo.class);
        } else {
            throw new CatNotFoundException();
        }
    }

    public List<CatInfo> findAllByGender(Gender gender){
        List<Cat> foundByGender = catRepository.findAllByGender(gender);
        return foundByGender.stream().map(cat -> modelMapper.map(cat, CatInfo.class)).collect(Collectors.toList());
    }

    public void catAdopted(Integer id){
        Cat toAdopt = catRepository.findById(id);
        Dog bestFriend = toAdopt.getBestFriendId().getDog();
        toAdopt.setAdopted(true);
        bestFriend.setAdopted(true);
        bestFriendRepository.saveAdoptedCat(toAdopt, bestFriend);
        catRepository.delete(toAdopt);
        dogRepository.delete(bestFriend);
    }

    public void catDeceased(Integer id){
        Cat toDelete = catRepository.findById(id);
        catRepository.delete(toDelete);
    }

    public List<CatInfo> whoNeedsToPlay(){
        List<Cat> allCats = catRepository.findAll();
        List<Cat> needToPlay = new ArrayList<>();
        for (Cat cat : allCats) {
            long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), cat.getLastPlay());
            if (hours > 6){
                needToPlay.add(cat);
            }
        }
        return needToPlay.stream().map(cat -> modelMapper.map(cat, CatInfo.class)).collect(Collectors.toList());
    }

    public CatInfo playWithCat(Integer id){
        Cat played = catRepository.playWithMeGirl(id);
        return modelMapper.map(played, CatInfo.class);
    }

    public void playWithAllCats(){
        for (Cat cat : catRepository.findAll()) {
            long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), cat.getLastPlay());
            if (hours > 6){
                cat.setLastPlay(LocalDateTime.now());
            }
        }
    }

    public DogInfo findBestFriend(Integer id) {
        return modelMapper.map(catRepository.findById(id).getBestFriendId().getDog(), DogInfo.class);
    }
}

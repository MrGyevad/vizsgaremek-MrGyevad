package hu.progmasters.animalShelter.service;

import hu.progmasters.animalShelter.domain.Animal;
import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.dto.AnimalInfo;
import hu.progmasters.animalShelter.dto.BestFriendInfo;
import hu.progmasters.animalShelter.exception.CatNotFoundException;
import hu.progmasters.animalShelter.exception.DogNotFoundException;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnimalService {

    private final DogRepository dogRepository;
    private final BestFriendRepository bestFriendRepository;
    private final CatRepository catRepository;
    private final ModelMapper modelMapper;

    public AnimalService(DogRepository dogRepository, BestFriendRepository bestFriendRepository, CatRepository catRepository, ModelMapper modelMapper) {
        this.dogRepository = dogRepository;
        this.bestFriendRepository = bestFriendRepository;
        this.catRepository = catRepository;
        this.modelMapper = modelMapper;
    }

    public List<AnimalInfo> findAllAnimals(){
        List<Animal> animals = new ArrayList<>();
        animals.addAll(dogRepository.findAll());
        animals.addAll(catRepository.findAll());
        return animals.stream().map(animal -> modelMapper.map(animal, AnimalInfo.class)).collect(Collectors.toList());
    }

    public BestFriendInfo becomeBestFriends(Integer catId, Integer dogId){
        Optional<Cat> catOptional = catRepository.findById(catId);
        Optional<Dog> dogOptional = dogRepository.findById(dogId);
        BestFriend toSave = new BestFriend();
        if (catOptional.isPresent()){
            if (dogOptional.isPresent()){
                BestFriend saved = bestFriendRepository.becomeBestFriends(toSave, catOptional.get(), dogOptional.get());
                return modelMapper.map(saved, BestFriendInfo.class);
            } else throw new DogNotFoundException("Dog not found.", dogId);
        } else throw new CatNotFoundException("Cat not found.", catId);
    }
}

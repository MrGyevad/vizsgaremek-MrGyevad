package hu.progmasters.animalShelter.service;

import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.dto.BestFriendInfo;
import hu.progmasters.animalShelter.dto.CatInfo;
import hu.progmasters.animalShelter.dto.DogInfo;
import hu.progmasters.animalShelter.exception.CatNotFoundException;
import hu.progmasters.animalShelter.exception.DogNotFoundException;
import hu.progmasters.animalShelter.exception.FriendShipNotFoundException;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(BestFriend.class, BestFriendInfo.class);
    }

    public List findAllAnimals(){
        return List.of(dogRepository.findAll().stream().map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList()),
                catRepository.findAll().stream().map(cat -> modelMapper.map(cat, CatInfo.class)).collect(Collectors.toList()));
    }

    public BestFriendInfo becomeBestFriends(Integer catId, Integer dogId){
        Optional<Cat> catOptional = catRepository.findById(catId);
        Optional<Dog> dogOptional = dogRepository.findById(dogId);
        if (catOptional.isPresent()){
            if (dogOptional.isPresent()){
                Optional<BestFriend> toSave = bestFriendRepository.findFriendshipById(dogOptional.get().getBestFriend().getId());
                if (toSave.isPresent()){
                    toSave.get().setCat(catOptional.get());
                    toSave.get().setDog(dogOptional.get());
                    bestFriendRepository.save(toSave.get());
                return modelMapper.map(toSave, BestFriendInfo.class);
                } else throw new FriendShipNotFoundException("Friendship not found.");
            } else throw new DogNotFoundException("Dog not found.", dogId);
        } else throw new CatNotFoundException("Cat not found.", catId);
    }
}

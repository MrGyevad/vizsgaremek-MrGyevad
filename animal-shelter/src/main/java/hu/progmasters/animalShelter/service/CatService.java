package hu.progmasters.animalShelter.service;

import hu.progmasters.animalShelter.domain.*;
import hu.progmasters.animalShelter.dto.CatCommand;
import hu.progmasters.animalShelter.dto.CatInfo;
import hu.progmasters.animalShelter.dto.DogInfo;
import hu.progmasters.animalShelter.exception.CatNotFoundException;
import hu.progmasters.animalShelter.exception.FriendShipNotFoundException;
import hu.progmasters.animalShelter.exception.NoBestFriendException;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class CatService {

    private final CatRepository catRepository;
    private final BestFriendRepository bestFriendRepository;
    private final AnimalShelterService animalShelterService;
    private final ModelMapper modelMapper;

    public CatService(CatRepository catRepository, BestFriendRepository bestFriendRepository, AnimalShelterService animalShelterService, ModelMapper modelMapper) {
        this.catRepository = catRepository;
        this.bestFriendRepository = bestFriendRepository;
        this.animalShelterService = animalShelterService;
        this.modelMapper = modelMapper;
    }

    public CatInfo saveCat(CatCommand command) {
        Cat toSave = modelMapper.map(command, Cat.class);
        toSave.setAnimalShelter(animalShelterService.findByIdForService(command.getAnimalShelterId()));
        Cat saved = catRepository.save(toSave);
        if (bestFriendRepository.findFriendshipById(saved.getId()).isPresent()) {
            BestFriend bestFriend = bestFriendRepository.findFriendshipById(saved.getId()).get();
            saved.setBestFriend(bestFriend);
            bestFriend.setCat(saved);
            bestFriendRepository.save(bestFriend);
        } else {
            BestFriend newBestFriend = new BestFriend(saved.getId(), saved, null);
            saved.setBestFriend(newBestFriend);
            bestFriendRepository.save(newBestFriend);
        }
        return modelMapper.map(catRepository.update(saved), CatInfo.class);
    }

    public CatInfo updateCat(Integer id, CatCommand command) {
        Cat toUpdate;
        if (catRepository.findById(id).isPresent()) {
            toUpdate = catRepository.findById(id).get();
            toUpdate.setAge(command.getAge());
            toUpdate.setBreed(command.getBreed());
            toUpdate.setName(command.getName());
            toUpdate.setAdopted(command.isAdopted());
            toUpdate.setGender(command.getGender());
            toUpdate.setHasWaterAndFood(command.isHasWaterAndFood());
            toUpdate.setLastPlay(command.getLastPlay());
            if (animalShelterService.findByIdForService(command.getAnimalShelterId()) != null) {
                animalShelterService.findByIdForService(command.getAnimalShelterId()).getCatList().add(toUpdate);
            }
        } else throw new CatNotFoundException("Cat not found.", id);
        return modelMapper.map(catRepository.update(toUpdate), CatInfo.class);
    }

    public List<CatInfo> findAllCats() {
        return catRepository.findAll()
                .stream()
                .map(cat -> modelMapper.map(cat, CatInfo.class)).collect(Collectors.toList());
    }


    public List<CatInfo> findAllAdoptedCats() {
        return catRepository.findAllAdopted()
                .stream()
                .map(cat -> modelMapper.map(cat, CatInfo.class)).collect(Collectors.toList());
    }

    public CatInfo findById(Integer id) {
        Optional<Cat> found = catRepository.findById(id);
        if (found.isPresent()) {
            return modelMapper.map(found.get(), CatInfo.class);
        } else throw new CatNotFoundException("Cat not found.", id);
    }

    public List<CatInfo> findAllByGender(Gender gender) {
        return catRepository.findAllByGender(gender).stream().map(cat -> modelMapper.map(cat, CatInfo.class)).collect(Collectors.toList());
    }

    public void catAdopted(Integer id) {
        Optional<Cat> toAdopt = catRepository.findById(id);
        Dog bestFriend;
        if (toAdopt.isPresent()) {
            toAdopt.get().setAdopted(true);
            bestFriend = toAdopt.get().getBestFriend().getDog();
            if (bestFriend != null) {
                bestFriend.setAdopted(true);
            } else throw new NoBestFriendException("Sadly, this cat has no best friend.", id);
        } else throw new CatNotFoundException("Cat not found.", id);
    }

    public void catDeceased(Integer id) {
        Optional<Cat> toDeleteOptional = catRepository.findById(id);
        if (toDeleteOptional.isPresent()) {
            Cat toDelete = toDeleteOptional.get();
            catRepository.catDeceased(toDelete);
        } else throw new CatNotFoundException("Cat not found.", id);
    }

    public List<CatInfo> whoNeedsToPlay() {
        List<Cat> allCats = catRepository.findAll();
        List<Cat> needToPlay = new ArrayList<>();
        for (Cat cat : allCats) {
            long hours = ChronoUnit.HOURS.between(cat.getLastPlay(), LocalDateTime.now());
            if (hours > 6) {
                needToPlay.add(cat);
            }
        } return needToPlay.stream().map(cat -> modelMapper.map(cat, CatInfo.class)).collect(Collectors.toList());
    }

    public CatInfo playWithCat(Integer id) throws InterruptedException {
        Cat cat;
        if (catRepository.findById(id).isPresent()) {
            cat = catRepository.findById(id).get();
            TimeUnit.MILLISECONDS.sleep(1);
            cat.setLastPlay(LocalDateTime.now());
            return modelMapper.map(cat, CatInfo.class);
        } else throw new CatNotFoundException("Cat not found.", id);
    }

    public void playWithAllCats() throws InterruptedException {
        for (Cat cat : catRepository.findAll()) {
            long hours = ChronoUnit.HOURS.between(cat.getLastPlay(), LocalDateTime.now());
            if (hours > 6) {
                TimeUnit.MILLISECONDS.sleep(1);
                cat.setLastPlay(LocalDateTime.now());
            }
        }
    }

    public DogInfo findBestFriend(Integer id) {
        Optional<Cat> found = catRepository.findById(id);
        if (found.isPresent()) {
            if (found.get().getBestFriend() != null) {
                if (found.get().getBestFriend().getDog() != null) {
                    return modelMapper.map(found.get().getBestFriend().getDog(), DogInfo.class);
                } else throw new NoBestFriendException("Sadly, this animal has no best friend.", id);
            } else throw new FriendShipNotFoundException("Friendship not found");
        } else throw new CatNotFoundException("Cat not found.", id);
    }
}

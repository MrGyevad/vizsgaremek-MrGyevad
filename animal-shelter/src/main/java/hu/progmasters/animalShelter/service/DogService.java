package hu.progmasters.animalShelter.service;

import hu.progmasters.animalShelter.domain.BestFriend;
import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.AnimalShelterInfo;
import hu.progmasters.animalShelter.dto.CatInfo;
import hu.progmasters.animalShelter.dto.DogCommand;
import hu.progmasters.animalShelter.dto.DogInfo;
import hu.progmasters.animalShelter.exception.DogNotFoundException;
import hu.progmasters.animalShelter.exception.FriendShipNotFoundException;
import hu.progmasters.animalShelter.exception.NoBestFriendException;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
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
public class DogService {

    private final DogRepository dogRepository;
    private final CatRepository catRepository;
    private final AnimalShelterService animalShelterService;
    private final BestFriendRepository bestFriendRepository;
    private final ModelMapper modelMapper;

    public DogService(DogRepository dogRepository, CatRepository catRepository, AnimalShelterService animalShelterService, BestFriendRepository bestFriendRepository, ModelMapper modelMapper) {
        this.dogRepository = dogRepository;
        this.catRepository = catRepository;
        this.animalShelterService = animalShelterService;
        this.bestFriendRepository = bestFriendRepository;
        this.modelMapper = modelMapper;

    }

    public DogInfo saveDog(DogCommand command) {
        Dog toSave = modelMapper.map(command, Dog.class);
        toSave.setAnimalShelter(animalShelterService.findByIdForService(command.getAnimalShelterId()));
        Dog saved = dogRepository.save(toSave);
        if (bestFriendRepository.findFriendshipById(saved.getId()).isPresent()){
            BestFriend bestFriend = bestFriendRepository.findFriendshipById(saved.getId()).get();
            saved.setBestFriend(bestFriend);
            bestFriend.setDog(saved);
            bestFriendRepository.save(bestFriend);
        } else {
            BestFriend newBestFriend = new BestFriend(saved.getId(), null, saved);
            saved.setBestFriend(newBestFriend);
            bestFriendRepository.save(newBestFriend);
        }
        return modelMapper.map(dogRepository.update(saved), DogInfo.class);
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
            dogToUpdate.setAnimalShelter(animalShelterService.findByIdForService(id));
            if (animalShelterService.findByIdForService(command.getAnimalShelterId()) != null){
            animalShelterService.findByIdForService(command.getAnimalShelterId()).getDogList().add(dogToUpdate);
            }
        } else {
            throw new DogNotFoundException("Dog not found.", id);
        }
        return modelMapper.map(dogRepository.update(dogToUpdate), DogInfo.class);
    }

    public List<DogInfo> findAllDogs() {
        return dogRepository.findAll().stream().map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
    }

    public List<DogInfo> findAllAdoptedDogs() {
        return dogRepository.findAllAdopted().stream().map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
    }

    public DogInfo findById(Integer id) {
        Optional<Dog> found = dogRepository.findById(id);
        if (found.isPresent()){
            return modelMapper.map(found.get(), DogInfo.class);
        } else {
            throw new DogNotFoundException("Dog not found.", id);
        }
    }

    public List<DogInfo> findAllByGender(Gender gender) {
        return dogRepository.findAllByGender(gender).stream().map(dog -> modelMapper.map(dog, DogInfo.class)).collect(Collectors.toList());
    }

    public void dogAdopted(Integer id) {
        Optional<Dog> toAdopt = dogRepository.findById(id);
        Cat bestFriend;
        if (toAdopt.isPresent()){
            toAdopt.get().setAdopted(true);
            bestFriend = toAdopt.get().getBestFriend().getCat();
            if (bestFriend != null){
                bestFriend.setAdopted(true);
            } else throw new NoBestFriendException("Sadly, this dog has no best friend.", id);
        } else throw new DogNotFoundException("Dog not found.", id);
    }

    public void dogDeceased(Integer id) {
        Optional<Dog> toDeleteOptional = dogRepository.findById(id);
        if (toDeleteOptional.isPresent()){
            Dog toDelete = toDeleteOptional.get();
            dogRepository.dogDeceased(toDelete);
        } else {
            throw new DogNotFoundException("Dog not found.", id);
        }
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

    public DogInfo walkTheDog(Integer id) throws InterruptedException {
        Dog dog;
        if (dogRepository.findById(id).isPresent()){
            dog = dogRepository.findById(id).get();
            TimeUnit.MILLISECONDS.sleep(1);
            dog.setLastWalk(LocalDateTime.now());

            return modelMapper.map(dogRepository.save(dog), DogInfo.class);
        } else {
            throw new DogNotFoundException("Dog not found.", id);
        }
    }


    public void walkAllDogs() throws InterruptedException{
        for (Dog dog : dogRepository.findAll()) {
            long hours = ChronoUnit.HOURS.between(dog.getLastWalk(), LocalDateTime.now());
            if (hours > 6) {
                TimeUnit.MILLISECONDS.sleep(1);
                dog.setLastWalk(LocalDateTime.now());
            }
        }
    }

    public CatInfo findBestFriend(Integer id) {
        Optional<Dog> found = dogRepository.findById(id);
        if (found.isPresent()){
            if (found.get().getBestFriend() != null){
                if (found.get().getBestFriend().getCat() != null){
                return modelMapper.map(found.get().getBestFriend().getCat(), CatInfo.class);
                } else throw new NoBestFriendException("Sadly, this animal has no best friend.", id);
            } else throw new FriendShipNotFoundException("Friendship not found");
        } else throw new DogNotFoundException("Dog not found.", id);

    }
}

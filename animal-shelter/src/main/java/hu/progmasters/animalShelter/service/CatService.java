package hu.progmasters.animalShelter.service;

import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.CatCommand;
import hu.progmasters.animalShelter.dto.CatInfo;
import hu.progmasters.animalShelter.dto.DogInfo;
import hu.progmasters.animalShelter.exception.CatNotFoundException;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.StrayRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CatService {

    private final CatRepository catRepository;
    private final StrayRepository strayRepository;
    private final ModelMapper modelMapper;

    public CatService(CatRepository catRepository, StrayRepository strayRepository, ModelMapper modelMapper) {
        this.catRepository = catRepository;
        this.strayRepository = strayRepository;
        this.modelMapper = modelMapper;
    }

    public CatInfo saveCat(CatCommand command){
        Cat toSave = modelMapper.map(command, Cat.class);
        Cat saved = catRepository.save(toSave);
        return modelMapper.map(saved, CatInfo.class);
    }

    public CatInfo updateCat(Integer id, CatCommand command){
        Cat toUpdate = catRepository.findById(id);
        toUpdate.setAge(command.getAge());
        toUpdate.setBreed(command.getBreed());
        toUpdate.setName(command.getName());
        toUpdate.setGoneStray(command.isGoneStray());
        toUpdate.setGender(command.getGender());
        toUpdate.setHasWaterAndFood(command.isHasWaterAndFood());
        toUpdate.setLastPlay(command.getLastPlay());
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

    public void catGoneStray(Integer id){
        Cat toDelete = catRepository.findById(id);
        toDelete.setGoneStray(true);
        strayRepository.saveGoneStray(toDelete);
        catRepository.delete(toDelete);
    }

    public CatInfo catHasBeenFound(Integer id){
        Cat found = (Cat) strayRepository.findById(id);
        Cat isAtHome = catRepository.update(found);
        return modelMapper.map(isAtHome, CatInfo.class);
    }
}

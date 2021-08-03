package hu.progmasters.animalShelter.service;

import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.DogCommand;
import hu.progmasters.animalShelter.dto.DogInfo;
import hu.progmasters.animalShelter.exception.DogNotFoundException;
import hu.progmasters.animalShelter.exception.NotLegitTimeException;
import hu.progmasters.animalShelter.repository.DogRepository;
import hu.progmasters.animalShelter.repository.StrayRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
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
        LocalDateTime ldt;
        if (!command.getLastWalkString().trim().equals("")){
            ldt = convertStringToLocalDateTime(command.getLastWalkString());
        } else {
            throw new NotLegitTimeException();
        }

        Dog toSave = new Dog();
        toSave.setLastWalk(ldt);
        toSave = modelMapper.map(command, Dog.class);
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
        toUpdate.setHasWaterAndFood(command.isHasWaterAndFood());
     //   toUpdate.setLastWalk(command.getLastWalk());
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
        Dog found = new Dog();
        try {
            found = (Dog) strayRepository.findById(id);
        } catch (DogNotFoundException dnfe) {
            dnfe.printStackTrace();
        }
        strayRepository.hasBeenFound(found);
        Dog isAtHome = dogRepository.save(found);

        return modelMapper.map(isAtHome, DogInfo.class);
    }

    public static LocalDateTime convertStringToLocalDateTime(String strDate) {
        String format = "yyyy-MM-dd hh:mm:ss";
        DateTimeFormatter DATE_TME_FORMATTER =
                new DateTimeFormatterBuilder().appendPattern(format)
                        .parseDefaulting(ChronoField.YEAR, 0)
                        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 0)
                        .parseDefaulting(ChronoField.DAY_OF_MONTH, 0)
                        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                        .toFormatter();
        LocalDateTime ldt = LocalDateTime.parse(strDate, DATE_TME_FORMATTER);
        return ldt;
    }
}

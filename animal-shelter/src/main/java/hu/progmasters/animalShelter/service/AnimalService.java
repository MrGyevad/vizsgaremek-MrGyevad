package hu.progmasters.animalShelter.service;

import hu.progmasters.animalShelter.domain.Animal;
import hu.progmasters.animalShelter.domain.Cat;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.dto.AnimalInfo;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnimalService {

    private final DogRepository dogRepository;
    private final CatRepository catRepository;
    private final ModelMapper modelMapper;

    public AnimalService(DogRepository dogRepository, CatRepository catRepository, ModelMapper modelMapper) {
        this.dogRepository = dogRepository;
        this.catRepository = catRepository;
        this.modelMapper = modelMapper;
    }

    public List<AnimalInfo> findAllAnimals(){
        List<Animal> animals = new ArrayList<>();
        animals.addAll(dogRepository.findAll());
        animals.addAll(catRepository.findAll());
        return animals.stream().map(animal -> modelMapper.map(animal, AnimalInfo.class)).collect(Collectors.toList());
    }
}

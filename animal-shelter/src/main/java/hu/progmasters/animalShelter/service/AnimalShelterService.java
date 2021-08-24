package hu.progmasters.animalShelter.service;

import hu.progmasters.animalShelter.domain.AnimalShelter;
import hu.progmasters.animalShelter.dto.AnimalShelterCommand;
import hu.progmasters.animalShelter.dto.AnimalShelterInfo;
import hu.progmasters.animalShelter.exception.AnimalShelterNotFoundException;
import hu.progmasters.animalShelter.repository.AnimalShelterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnimalShelterService {

    private final AnimalShelterRepository animalShelterRepository;
    private final ModelMapper modelMapper;

    public AnimalShelterService(AnimalShelterRepository animalShelterRepository, ModelMapper modelMapper) {
        this.animalShelterRepository = animalShelterRepository;
        this.modelMapper = modelMapper;
    }

    public AnimalShelterInfo saveAnimalShelter(AnimalShelterCommand command){
        AnimalShelter toSave = modelMapper.map(command, AnimalShelter.class);
        AnimalShelter saved = animalShelterRepository.save(toSave);
        return modelMapper.map(saved, AnimalShelterInfo.class);
    }

    public AnimalShelterInfo findById(Integer id){
        Optional<AnimalShelter> found = animalShelterRepository.findById(id);
        if (found.isPresent()){
            return modelMapper.map(found.get(), AnimalShelterInfo.class);
        } else throw new AnimalShelterNotFoundException("Shelter not found", id);
    }
    public AnimalShelter findByIdForService(Integer id){
        Optional<AnimalShelter> found = animalShelterRepository.findById(id);
        if (found.isPresent()){
            return found.get();
        } else throw new AnimalShelterNotFoundException("Shelter not found", id);
    }

    public List<AnimalShelterInfo> findAll(){
        return animalShelterRepository.findAll()
                .stream()
                .map(animalShelter -> modelMapper.map(animalShelter, AnimalShelterInfo.class))
                .collect(Collectors.toList());
    }

    public AnimalShelterInfo update(Integer id, AnimalShelterCommand command){
        AnimalShelter toUpdate;
        if (animalShelterRepository.findById(id).isPresent()){
            toUpdate = animalShelterRepository.findById(id).get();
            toUpdate.setName(command.getName());
        } else throw new AnimalShelterNotFoundException("Shelter not found", id);
        return modelMapper.map(animalShelterRepository.update(toUpdate), AnimalShelterInfo.class);
    }

    public void delete(Integer id){
        AnimalShelter toDelete;
        if (animalShelterRepository.findById(id).isPresent()){
            toDelete = animalShelterRepository.findById(id).get();
            animalShelterRepository.delete(toDelete);
        }
    }
}


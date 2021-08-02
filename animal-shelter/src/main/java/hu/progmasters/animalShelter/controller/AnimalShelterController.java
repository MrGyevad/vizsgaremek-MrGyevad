package hu.progmasters.animalShelter.controller;

import hu.progmasters.animalShelter.dto.*;
import hu.progmasters.animalShelter.service.AnimalService;
import hu.progmasters.animalShelter.service.CatService;
import hu.progmasters.animalShelter.service.DogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class AnimalShelterController {

    private final DogService dogService;
    private final CatService catService;
    private final AnimalService animalService;

    public AnimalShelterController(DogService dogService, CatService catService, AnimalService animalService) {
        this.dogService = dogService;
        this.catService = catService;
        this.animalService = animalService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<AnimalInfo> findAllResidents(){
        List<AnimalInfo> animalInfoList;
        log.info("HTTP GET /api - List all animals");
        animalInfoList = animalService.findAllAnimals();
        log.info(String.format("HTTP Response: FOUND, Body: %s", animalInfoList));
        return animalInfoList;
    }

    @GetMapping("/dog")
    @ResponseStatus(HttpStatus.FOUND)
    public List<DogInfo> findAllDogs(){
        List<DogInfo> dogInfoList;
        log.info("HTTP GET /api/dog - List all dogs");
        dogInfoList = dogService.findAllDogs();
        log.info(String.format("HTTP Response: FOUND, Body: %s", dogInfoList));
        return dogInfoList;
    }

    @PostMapping("/dog")
    @ResponseStatus(HttpStatus.CREATED)
    public DogInfo saveDog(@RequestBody DogCommand command){
        log.info("HTTP POST /api/dog - Save a dog");
        DogInfo toSave = dogService.saveDog(command);
        log.info(String.format("HTTP Response: CREATED, Body: %s", toSave));
        return toSave;
    }

    @GetMapping("/dog/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public DogInfo findDogById(@PathVariable("id") Integer id){
        log.info(String.format("HTTP GET /api/dog/%s - Find dog by ID", id));
        DogInfo found = dogService.findById(id);
        log.info(String.format("HTTP Response: FOUND, Body: %s", found));
        return found;
    }

    @PutMapping("/dog/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DogInfo updateDog(@PathVariable("id") Integer id, @RequestBody DogCommand command){
        log.info(String.format("HTTP PUT /api/dog/%s - Update dog", id));
        DogInfo updated = dogService.updateDog(id, command);
        log.info("HTTP Response: OK, Dog updated.");
        return updated;
    }

    @DeleteMapping("/dog/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void dogGoneStray(@PathVariable("id") Integer id){
        log.info(String.format("HTTP DELETE /api/dog/%s - Delete dog", id));
        dogService.dogGoneStray(id);
        log.info("HTTP Response: OK, Dog deleted from database.");
    }

    @PutMapping("/dog/find/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DogInfo dogHasBeenFound(@PathVariable("id") Integer id){
        log.info(String.format("HTTP PUT /api/dog/%s - Find stray dog", id));
        DogInfo found = dogService.dogHasBeenFound(id);
        log.info(String.format("HTTP Response: OK, This dog was found behind the warehouse:", found));
        return found;
    }

    @GetMapping("/cat")
    @ResponseStatus(HttpStatus.FOUND)
    public List<CatInfo> findAllCats(){
        List<CatInfo> catInfoList;
        log.info("HTTP GET /api/cat - List all cats");
        catInfoList = catService.findAllCats();
        log.info(String.format("HTTP Response: FOUND, Body: %s", catInfoList));
        return catInfoList;
    }

    @GetMapping("/cat/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public CatInfo findCatById(@PathVariable("id") Integer id){
        log.info(String.format("HTTP GET /api/cat/%s - Find cat by ID", id));
        CatInfo found = catService.findById(id);
        log.info(String.format("HTTP Response: FOUND, Body: %s", found));
        return found;
    }

    @PostMapping("/cat")
    @ResponseStatus(HttpStatus.CREATED)
    public CatInfo saveCat(@RequestBody CatCommand command){
        log.info("HTTP POST /api/cat - Save a cat");
        CatInfo toSave = catService.saveCat(command);
        log.info(String.format("HTTP Response: CREATED, Body: %s", toSave));
        return toSave;
    }

    @PutMapping("/cat/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CatInfo updateCat(@PathVariable Integer id, @RequestBody CatCommand command){
        log.info(String.format("HTTP PUT /api/cat/%s - Update cat with info %s", id, command));
        CatInfo updated = catService.updateCat(id, command);
        log.info(String.format("HTTP Response: OK, Body: %s", updated));
        return updated;
    }

    



}

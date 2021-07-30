package hu.progmasters.animalShelter.controller;

import hu.progmasters.animalShelter.dto.CatInfo;
import hu.progmasters.animalShelter.dto.DogInfo;
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

    public AnimalShelterController(DogService dogService, CatService catService) {
        this.dogService = dogService;
        this.catService = catService;
    }

    @GetMapping("/dog")
    @ResponseStatus(HttpStatus.OK)
    public List<DogInfo> findAllDogs(){
        List<DogInfo> dogInfoList;
        log.info("HTTP GET /api/dog - List all dogs");
        dogInfoList = dogService.findAllDogs();
        log.info(String.format("HTTP Response: OK, Body: %s", dogInfoList));
        return dogInfoList;
    }

    @GetMapping("/cat")
    @ResponseStatus(HttpStatus.OK)
    public List<CatInfo> findAllCats(){
        List<CatInfo> catInfoList;
        log.info("HTTP GET /api/cat - List all cats");
        catInfoList = catService.findAllCats();
        log.info(String.format("HTTP Response: OK, Body: %s", catInfoList));
        return catInfoList;
    }


}

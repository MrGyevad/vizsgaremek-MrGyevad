package hu.progmasters.animalShelter.controller;

import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.*;
import hu.progmasters.animalShelter.exception.CatNotFoundException;
import hu.progmasters.animalShelter.exception.DogNotFoundException;
import hu.progmasters.animalShelter.service.AnimalService;
import hu.progmasters.animalShelter.service.CatService;
import hu.progmasters.animalShelter.service.DogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public DogInfo saveDog(@Valid @RequestBody DogCommand command){
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
    public DogInfo updateDog(@PathVariable("id") Integer id, @Valid @RequestBody DogCommand command){
        log.info(String.format("HTTP PUT /api/dog/%s - Update dog", id));
        DogInfo updated = new DogInfo();
        try {
            updated = dogService.updateDog(id, command);
        } catch (DogNotFoundException e){
            e.printStackTrace();
        }
        log.info("HTTP Response: OK, Dog updated.");
        return updated;
    }

    @PutMapping("/dog/adopt/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void dogAdopted(@PathVariable("id") Integer id){
        log.info(String.format("HTTP PUT /api/dog/adopt/%s - Adopt dog", id));
        dogService.dogAdopted(id);
        log.info("HTTP Response: OK, Dog adopted from the shelter.");
    }

    @DeleteMapping("/dog/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void dogDeceased(@PathVariable("id") Integer id){
        log.info(String.format("HTTP DELETE /api/dog/%s - Delete dog from repository", id));
        dogService.dogDeceased(id);
        log.info("HTTP Response: OK, The dog has deceased:");
    }

    @GetMapping("/dog/walk")
    @ResponseStatus(HttpStatus.OK)
    public List<DogInfo> whoNeedsToWalk(){
        log.info("HTTP GET /api/dog/walk - List dogs who need to walk");
        List<DogInfo> needToWalkList = dogService.whoNeedsAWalk();
        log.info(String.format("HTTP Response OK, These dogs need to walk: %s", needToWalkList));
        return needToWalkList;
    }

    @PutMapping("/dog/walk/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DogInfo walkWithMe(@PathVariable("id") Integer id){
        log.info(String.format("HTTP PUT /api/dog/walk/%s - Walk dog with ID", id));
        DogInfo walked = dogService.walkTheDog(id);
        log.info(String.format("HTTP Response OK, This dog walked: %s", walked));
        return walked;
    }

    @GetMapping("/dog/gender/{gender}")
    @ResponseStatus(HttpStatus.OK)
    public List<DogInfo> findAllDogsByGender(@PathVariable("gender") Gender gender){
        log.info("HTTP GET /api/dog/gender - Find dogs by gender");
        List<DogInfo> genderList = dogService.findAllByGender(gender);
        log.info(String.format("HTTP Response OK, These are the %s dogs: %s", gender, genderList));
        return genderList;
    }

    @PutMapping("/dog/walk")
    @ResponseStatus(HttpStatus.OK)
    public void walkWithAllDogs(){
        log.info("HTTP PUT /api/dog/walk - Walk with all dogs");
        dogService.walkAllDogs();
        log.info("HTTP Response OK, Walked with all dogs, who needed to walk.");
    }

    @GetMapping("/dog/bestfriend/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CatInfo findDogsBestFriend(@PathVariable("id") Integer id){
        CatInfo bestFriend = new CatInfo();
        log.info("HTTP GET /api/dog/bestfriend - Find dog's best friend");
        String dogName = dogService.findById(id).getName();
        try {
            bestFriend = dogService.findBestFriend(id);
        }catch (CatNotFoundException e){
            e.printStackTrace();
        }
        log.info(String.format("HTTP Response OK, The best friend of %s is %s", dogName, bestFriend));
        return bestFriend;
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

    @PostMapping("/cat")
    @ResponseStatus(HttpStatus.CREATED)
    public CatInfo saveCat(@Valid @RequestBody CatCommand command){
        log.info("HTTP POST /api/cat - Save a cat");
        CatInfo toSave = catService.saveCat(command);
        log.info(String.format("HTTP Response: CREATED, Body: %s", toSave));
        return toSave;
    }

    @GetMapping("/cat/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public CatInfo findCatById(@PathVariable("id") Integer id){
        log.info(String.format("HTTP GET /api/cat/%s - Find cat by ID", id));
        CatInfo found = catService.findById(id);
        log.info(String.format("HTTP Response: FOUND, Body: %s", found));
        return found;
    }

    @PutMapping("/cat/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CatInfo updateCat(@PathVariable Integer id, @Valid @RequestBody CatCommand command){
        log.info(String.format("HTTP PUT /api/cat/%s - Update cat with info %s", id, command));
        CatInfo updated = catService.updateCat(id, command);
        log.info(String.format("HTTP Response: OK, Body: %s", updated));
        return updated;
    }

    @PutMapping("/cat/adopt/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void catAdopted(@PathVariable("id") Integer id){
        log.info(String.format("HTTP PUT /api/cat/adopt/%s - Adopt cat", id));
        catService.catAdopted(id);
        log.info("HTTP Response: OK, Cat adopted from the shelter.");
    }

    @DeleteMapping("/cat/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void catDeceased(@PathVariable("id") Integer id){
        log.info(String.format("HTTP DELETE /api/cat/%s - Delete cat from repository", id));
        catService.catDeceased(id);
        log.info("HTTP Response: OK, The cat has deceased:");
    }

    @GetMapping("/cat/play")
    @ResponseStatus(HttpStatus.OK)
    public List<CatInfo> whoNeedsToPlay(){
        log.info("HTTP GET /api/cat/play - List cats who need to play");
        List<CatInfo> needToPlayList = catService.whoNeedsToPlay();
        log.info(String.format("HTTP Response OK, These cats need to play: %s", needToPlayList));
        return needToPlayList;
    }

    @PutMapping("/cat/play/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CatInfo playWithMe(@PathVariable("id") Integer id){
        log.info(String.format("HTTP PUT /api/cat/play/%s - Play cat with ID", id));
        CatInfo played = catService.playWithCat(id);
        log.info(String.format("HTTP Response OK, This cat played: %s", played));
        return played;
    }

    @GetMapping("/cat/gender/{gender}")
    @ResponseStatus(HttpStatus.OK)
    public List<CatInfo> findAllCatsByGender(@PathVariable("gender") Gender gender){
        log.info("HTTP GET /api/cat/gender - Find cats by gender");
        List<CatInfo> genderList = catService.findAllByGender(gender);
        log.info(String.format("HTTP Response OK, These are the %s cats: %s", gender, genderList));
        return genderList;
    }

    @PutMapping("/cat/play")
    @ResponseStatus(HttpStatus.OK)
    public void playWithAllCats(){
        log.info("HTTP PUT /api/cat/play - Play with all cats");
        catService.playWithAllCats();
        log.info("HTTP Response OK, Played with all cats, who needed to play.");
    }

    @GetMapping("/cat/bestfriend/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DogInfo findCatsBestFriend(@PathVariable("id") Integer id){
        log.info("HTTP GET /api/cat/bestfriend - Find cat's best friend");
        String catName = catService.findById(id).getName();
        DogInfo bestFriend = catService.findBestFriend(id);
        log.info(String.format("HTTP Response OK, The best friend of %s is %s", catName, bestFriend));
        return bestFriend;
    }

}

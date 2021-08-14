package hu.progmasters.animalShelter.controller;

import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.*;
import hu.progmasters.animalShelter.exception.AnimalShelterError;
import hu.progmasters.animalShelter.exception.CatNotFoundException;
import hu.progmasters.animalShelter.exception.DogNotFoundException;
import hu.progmasters.animalShelter.exception.ValidationError;
import hu.progmasters.animalShelter.service.AnimalService;
import hu.progmasters.animalShelter.service.CatService;
import hu.progmasters.animalShelter.service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Find all animals")
    @ApiResponse(responseCode = "200",
            description = "List all animals living in the shelter.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = AnimalInfo.class))))
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AnimalInfo> findAllResidents() {
        List<AnimalInfo> animalInfoList;
        log.info("HTTP GET /api - List all animals");
        animalInfoList = animalService.findAllAnimals();
        log.info(String.format("HTTP Response: FOUND, Body: %s", animalInfoList));
        return animalInfoList;
    }

    @Operation(summary = "Find all dogs")
    @ApiResponse(responseCode = "200",
            description = "List all dogs living in the shelter",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = DogInfo.class))))
    @GetMapping("/dog")
    @ResponseStatus(HttpStatus.OK)
    public List<DogInfo> findAllDogs() {
        List<DogInfo> dogInfoList;
        log.info("HTTP GET /api/dog - List all dogs");
        dogInfoList = dogService.findAllDogs();
        log.info(String.format("HTTP Response: OK, Body: %s", dogInfoList));
        return dogInfoList;
    }

    @Operation(summary = "Find all adopted dogs")
    @ApiResponse(responseCode = "200",
            description = "List all dogs who once lived in the shelter",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = DogInfo.class))))
    @GetMapping("/dog/adopt")
    @ResponseStatus(HttpStatus.OK)
    public List<DogInfo> findAllAdoptedDogs() {
        List<DogInfo> dogInfoList;
        log.info("HTTP GET /api/dog - List all adopted dogs");
        dogInfoList = dogService.findAllAdoptedDogs();
        log.info(String.format("HTTP Response: OK, Body: %s", dogInfoList));
        return dogInfoList;
    }

    @Operation(summary = "Save a new dog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Dog created.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DogInfo.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request, validation error.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = AnimalShelterError.class))))})
    @PostMapping("/dog")
    @ResponseStatus(HttpStatus.CREATED)
    public DogInfo saveDog(@Valid @RequestBody DogCommand command) {
        log.info("HTTP POST /api/dog - Save a dog");
        DogInfo toSave = dogService.saveDog(command);
        log.info(String.format("HTTP Response: CREATED, Body: %s", toSave));
        return toSave;
    }

    @Operation(summary = "Find dog by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Found dog by given ID.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DogInfo.class))),
        @ApiResponse(responseCode = "404",
                description = "Dog not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = AnimalShelterError.class))))})
    @GetMapping("/dog/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DogInfo findDogById(@PathVariable("id") Integer id) {
        log.info(String.format("HTTP GET /api/dog/%s - Find dog by ID", id));
        DogInfo found = dogService.findById(id);
        log.info(String.format("HTTP Response: FOUND, Body: %s", found));
        return found;
    }

    @Operation(summary = "Update a dog")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated the dog.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DogInfo.class))),
            @ApiResponse(responseCode = "404",
                    description = "Dog not found.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalShelterError.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request, validation error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ValidationError.class)))})
    @PutMapping("/dog/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DogInfo updateDog(@PathVariable("id") Integer id, @Valid @RequestBody DogCommand command) {
        log.info(String.format("HTTP PUT /api/dog/%s - Update dog", id));
        DogInfo updated = dogService.updateDog(id, command);
        log.info("HTTP Response: OK, Dog updated.");
        return updated;
    }

    @Operation(summary = "Adopt a dog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Dog successfully adopted, along with it's best friend"),
            @ApiResponse(responseCode = "404",
                    description = "Dog not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AnimalShelterError.class)))})
    @PutMapping("/dog/adopt/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void dogAdopted(@PathVariable("id") Integer id) {
        log.info(String.format("HTTP PUT /api/dog/adopt/%s - Adopt dog", id));
        dogService.dogAdopted(id);
        log.info("HTTP Response: OK, Dog adopted from the shelter.");
    }

    @Operation(summary = "Delete dog from the shelter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Dog deleted from the shelter."),
            @ApiResponse(responseCode = "404",
                    description = "Dog not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AnimalShelterError.class)))})
    @DeleteMapping("/dog/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void dogDeceased(@PathVariable("id") Integer id) {
        log.info(String.format("HTTP DELETE /api/dog/%s - Delete dog from repository", id));
        dogService.dogDeceased(id);
        log.info("HTTP Response: OK, The dog has deceased:");
    }

    @Operation(summary = "List all dogs, who need a walk.")
    @ApiResponse(responseCode = "200",
            description = "Listed all dogs, who need a walk.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = DogInfo.class))))
    @GetMapping("/dog/walk")
    @ResponseStatus(HttpStatus.OK)
    public List<DogInfo> whoNeedsToWalk() {
        log.info("HTTP GET /api/dog/walk - List dogs who need to walk");
        List<DogInfo> needToWalkList = dogService.whoNeedsAWalk();
        log.info(String.format("HTTP Response OK, These dogs need to walk: %s", needToWalkList));
        return needToWalkList;
    }

    @Operation(summary = "Walk a dog")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Walked the dog with the given ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DogInfo.class))),
            @ApiResponse(responseCode = "404",
                    description = "Dog not found.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AnimalShelterError.class)))})
    @PutMapping("/dog/walk/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DogInfo walkWithMe(@PathVariable("id") Integer id) {
        log.info(String.format("HTTP PUT /api/dog/walk/%s - Walk dog with ID", id));
        DogInfo walked = dogService.walkTheDog(id);
        log.info(String.format("HTTP Response OK, This dog walked: %s", walked));
        return walked;
    }

    @Operation(summary = "Find all dogs by gender")
    @ApiResponse(responseCode = "200",
            description = "List all dogs by given gender.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = DogInfo.class))))
    @GetMapping("/dog/gender/{gender}")
    @ResponseStatus(HttpStatus.OK)
    public List<DogInfo> findAllDogsByGender(@PathVariable("gender") Gender gender) {
        log.info("HTTP GET /api/dog/gender - Find dogs by gender");
        List<DogInfo> genderList = dogService.findAllByGender(gender);
        log.info(String.format("HTTP Response OK, These are the %s dogs: %s", gender, genderList));
        return genderList;
    }

    @Operation(summary = "Walk all dogs, who need a walk")
    @ApiResponse(responseCode = "200",
            description = "Walked all dogs, who needed a walk")
    @PutMapping("/dog/walk")
    @ResponseStatus(HttpStatus.OK)
    public void walkWithAllDogs() {
        log.info("HTTP PUT /api/dog/walk - Walk with all dogs");
        dogService.walkAllDogs();
        log.info("HTTP Response OK, Walked with all dogs, who needed to walk.");
    }

    @Operation(summary = "Find the best friend of a dog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the best friend of the dog",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CatInfo.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Dog not found, or has no best friend",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AnimalShelterError.class)))})
    @GetMapping("/dog/bestFriend/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CatInfo findDogsBestFriend(@PathVariable("id") Integer id) {
        CatInfo bestFriend = new CatInfo();
        log.info("HTTP GET /api/dog/bestfriend - Find dog's best friend");
        String dogName = dogService.findById(id).getName();
        try {
            bestFriend = dogService.findBestFriend(id);
        } catch (CatNotFoundException e) {
            e.printStackTrace();
        }
        log.info(String.format("HTTP Response OK, The best friend of %s is %s", dogName, bestFriend));
        return bestFriend;
    }

    @Operation(summary = "Find all cats")
    @ApiResponse(responseCode = "200",
            description = "Listed all cats living in the shelter",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = CatInfo.class))))
    @GetMapping("/cat")
    @ResponseStatus(HttpStatus.FOUND)
    public List<CatInfo> findAllCats() {
        List<CatInfo> catInfoList;
        log.info("HTTP GET /api/cat - List all cats");
        catInfoList = catService.findAllCats();
        log.info(String.format("HTTP Response: FOUND, Body: %s", catInfoList));
        return catInfoList;
    }

    @Operation(summary = "Save a new cat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Cat created.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CatInfo.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request, validation error.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AnimalShelterError.class))))})
    @PostMapping("/cat")
    @ResponseStatus(HttpStatus.CREATED)
    public CatInfo saveCat(@Valid @RequestBody CatCommand command) {
        log.info("HTTP POST /api/cat - Save a cat");
        CatInfo toSave = catService.saveCat(command);
        log.info(String.format("HTTP Response: CREATED, Body: %s", toSave));
        return toSave;
    }

    @Operation(summary = "Find cat by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found cat by given ID.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CatInfo.class))),
            @ApiResponse(responseCode = "404",
                    description = "Cat not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AnimalShelterError.class))))})
    @GetMapping("/cat/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public CatInfo findCatById(@PathVariable("id") Integer id) {
        log.info(String.format("HTTP GET /api/cat/%s - Find cat by ID", id));
        CatInfo found = catService.findById(id);
        log.info(String.format("HTTP Response: FOUND, Body: %s", found));
        return found;
    }

    @Operation(summary = "Update a cat")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated the cat.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CatInfo.class))),
            @ApiResponse(responseCode = "404",
                    description = "Cat not found.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalShelterError.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request, validation error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ValidationError.class)))})
    @PutMapping("/cat/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CatInfo updateCat(@PathVariable Integer id, @Valid @RequestBody CatCommand command) {
        log.info(String.format("HTTP PUT /api/cat/%s - Update cat with info %s", id, command));
        CatInfo updated = catService.updateCat(id, command);
        log.info(String.format("HTTP Response: OK, Body: %s", updated));
        return updated;
    }

    @Operation(summary = "Adopt a cat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cat successfully adopted, along with it's best friend"),
            @ApiResponse(responseCode = "404",
                    description = "Cat not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalShelterError.class)))})
    @PutMapping("/cat/adopt/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void catAdopted(@PathVariable("id") Integer id) {
        log.info(String.format("HTTP PUT /api/cat/adopt/%s - Adopt cat", id));
        catService.catAdopted(id);
        log.info("HTTP Response: OK, Cat adopted from the shelter.");
    }

    @Operation(summary = "Delete cat from the shelter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cat deleted from the shelter."),
            @ApiResponse(responseCode = "404",
                    description = "Cat not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalShelterError.class)))})
    @DeleteMapping("/cat/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void catDeceased(@PathVariable("id") Integer id) {
        log.info(String.format("HTTP DELETE /api/cat/%s - Delete cat from repository", id));
        catService.catDeceased(id);
        log.info("HTTP Response: OK, The cat has deceased:");
    }

    @Operation(summary = "List all cats, who need to play.")
    @ApiResponse(responseCode = "200",
            description = "Listed all cats, who need to play.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = CatInfo.class))))
    @GetMapping("/cat/play")
    @ResponseStatus(HttpStatus.OK)
    public List<CatInfo> whoNeedsToPlay() {
        log.info("HTTP GET /api/cat/play - List cats who need to play");
        List<CatInfo> needToPlayList = catService.whoNeedsToPlay();
        log.info(String.format("HTTP Response OK, These cats need to play: %s", needToPlayList));
        return needToPlayList;
    }

    @Operation(summary = "Play with a cat")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Played with the cat with the given ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CatInfo.class))),
            @ApiResponse(responseCode = "404",
                    description = "Cat not found.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalShelterError.class)))})
    @PutMapping("/cat/play/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CatInfo playWithMe(@PathVariable("id") Integer id) {
        log.info(String.format("HTTP PUT /api/cat/play/%s - Play cat with ID", id));
        CatInfo played = catService.playWithCat(id);
        log.info(String.format("HTTP Response OK, This cat played: %s", played));
        return played;
    }

    @Operation(summary = "Find all cats by gender")
    @ApiResponse(responseCode = "200",
            description = "List all cats by given gender.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = CatInfo.class))))
    @GetMapping("/cat/gender/{gender}")
    @ResponseStatus(HttpStatus.OK)
    public List<CatInfo> findAllCatsByGender(@PathVariable("gender") Gender gender) {
        log.info("HTTP GET /api/cat/gender - Find cats by gender");
        List<CatInfo> genderList = catService.findAllByGender(gender);
        log.info(String.format("HTTP Response OK, These are the %s cats: %s", gender, genderList));
        return genderList;
    }

    @Operation(summary = "Play with all cats, who need to play")
    @ApiResponse(responseCode = "200",
            description = "Played with all cats, who needed to play")
    @PutMapping("/cat/play")
    @ResponseStatus(HttpStatus.OK)
    public void playWithAllCats() {
        log.info("HTTP PUT /api/cat/play - Play with all cats");
        catService.playWithAllCats();
        log.info("HTTP Response OK, Played with all cats, who needed to play.");
    }

    @Operation(summary = "Find the best friend of a cat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the best friend of the cat",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DogInfo.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Cat not found, or has no best friend",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalShelterError.class)))})
    @GetMapping("/cat/bestfriend/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DogInfo findCatsBestFriend(@PathVariable("id") Integer id) {
        DogInfo bestFriend = catService.findBestFriend(id);
        log.info(String.format("HTTP GET /api/cat/bestfriend/%s - Find cat's best friend", id));
        String catName = catService.findById(id).getName();
        try {
            bestFriend = catService.findBestFriend(id);
        } catch (DogNotFoundException e) {
            e.printStackTrace();
        }
        log.info(String.format("HTTP Response OK, The best friend of %s is %s", catName, bestFriend));
        return bestFriend;
    }


    @PutMapping("/{catId}/{dogId}")
    @ResponseStatus(HttpStatus.OK)
    public BestFriendInfo becomeBestFriends(@PathVariable("catId") Integer catId, @PathVariable("dogId") Integer dogId){
        log.info(String.format("HTTP PUT /api/%s/%s - Cat and dog become best friends", catId, dogId));
        BestFriendInfo bestFriendInfo = animalService.becomeBestFriends(catId, dogId);
        log.info(String.format("HTTP Response OK, Cat with ID: %s, and dog with ID: %s became best friends.", catId, dogId));
        return bestFriendInfo;

    }

}

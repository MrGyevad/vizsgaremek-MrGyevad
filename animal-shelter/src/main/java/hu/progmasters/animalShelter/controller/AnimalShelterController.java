package hu.progmasters.animalShelter.controller;

import hu.progmasters.animalShelter.dto.*;
import hu.progmasters.animalShelter.exception.AnimalShelterError;
import hu.progmasters.animalShelter.exception.ValidationError;
import hu.progmasters.animalShelter.service.AnimalShelterService;
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
@RequestMapping("/api/animalShelter")
@Slf4j
public class AnimalShelterController {


    private final AnimalShelterService animalShelterService;

    public AnimalShelterController(AnimalShelterService animalShelterService) {
        this.animalShelterService = animalShelterService;
    }

    @Operation(summary = "Save a new animal shelter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "The animal shelter was created.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalShelterInfo.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request, validation error.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ValidationError.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalShelterInfo saveAnimalShelter(@Valid @RequestBody AnimalShelterCommand command) {
        log.info("HTTP POST /api/animalShelter - Save a new shelter");
        AnimalShelterInfo saved = animalShelterService.saveAnimalShelter(command);
        log.info(String.format("HTTP Response: CREATED, Body: %s", saved));
        return saved;
    }

    @Operation(summary = "Find all animal shelters")
    @ApiResponse(responseCode = "200",
            description = "Listed all animal shelters.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = AnimalShelterInfo.class)))
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AnimalShelterInfo> findAll() {
        List<AnimalShelterInfo> animalShelterInfoList;
        log.info("HTTP GET /api/animalShelter - List all shelters");
        animalShelterInfoList = animalShelterService.findAll();
        log.info("HTTP Response OK - Listed all animalShelters.");
        return animalShelterInfoList;
    }

    @Operation(summary = "Find animal shelter by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the animal shelter by given ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalShelterInfo.class))),
            @ApiResponse(responseCode = "404",
                    description = "Animal shelter not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AnimalShelterError.class))))})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnimalShelterInfo findById(@PathVariable("id") Integer id) {
        log.info(String.format("HTTP GET /api/animalShelter/{%s} - Find animalShelter by ID", id));
        AnimalShelterInfo found = animalShelterService.findById(id);
        log.info(String.format("HTTP Response OK - Found the animalShelter. Body: %s", found));
        return found;
    }

    @Operation(summary = "Update the animal shelter")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated the animal shelter.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalShelterInfo.class))),
            @ApiResponse(responseCode = "404",
                    description = "Animal shelter not found.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalShelterError.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request, validation error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ValidationError.class)))})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnimalShelterInfo update(@PathVariable("id") Integer id, @Valid @RequestBody AnimalShelterCommand command) {
        log.info(String.format("HTTP PUT /api/{%s} - Update the animalShelter", id));
        AnimalShelterInfo updated = animalShelterService.update(id, command);
        log.info(String.format("HTTP Response: OK - Updated the shelter with Id: %s", id));
        return updated;
    }

    @Operation(summary = "Delete animal shelter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Animal shelter deleted."),
            @ApiResponse(responseCode = "404",
                    description = "Animal shelter not found.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AnimalShelterError.class)))})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Integer id) {
        log.info("HTTP DELETE /api/{id} - Delete the shelter with the given ID");
        animalShelterService.delete(id);
    }
}

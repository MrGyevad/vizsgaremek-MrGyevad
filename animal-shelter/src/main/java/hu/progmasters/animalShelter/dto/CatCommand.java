package hu.progmasters.animalShelter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.validation.CatGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatCommand {

    @NotBlank(message = "Name must not be null, empty, or whitespace only.")
    private String name;
    @PositiveOrZero
    private int age;
    @NotBlank(message = "Breed must not be null, empty, or whitespace only.")
    private String breed;
    @CatGender
    private Gender gender;
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime lastPlay;
    @NotNull
    @AssertTrue
    private boolean hasWaterAndFood;
    @NotNull
    @AssertFalse
    private boolean adopted;
}

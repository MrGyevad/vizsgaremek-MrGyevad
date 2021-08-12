package hu.progmasters.animalShelter.dto;

import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.validation.CatGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
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
    private LocalDateTime lastPlay;
    private boolean hasWaterAndFood;
    private boolean adopted;
}

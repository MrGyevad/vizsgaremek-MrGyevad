package hu.progmasters.animalShelter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalShelterCommand {

    @NotBlank(message = "Name must not be null, empty, or whitespace only.")
    private String name;
}

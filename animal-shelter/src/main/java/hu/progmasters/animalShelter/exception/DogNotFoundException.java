package hu.progmasters.animalShelter.exception;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DogNotFoundException extends RuntimeException {
    private String errorMessage = "Dog not found.";
    private Integer idOfNotFound;
}

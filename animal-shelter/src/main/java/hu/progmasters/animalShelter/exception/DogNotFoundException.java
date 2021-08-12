package hu.progmasters.animalShelter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DogNotFoundException extends RuntimeException {
    private String field;
    private Integer idOfNotFound;
}

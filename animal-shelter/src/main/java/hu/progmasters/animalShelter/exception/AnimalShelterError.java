package hu.progmasters.animalShelter.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalShelterError {
    private String field;
    private String message;
}

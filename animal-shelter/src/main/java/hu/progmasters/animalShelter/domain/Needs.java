package hu.progmasters.animalShelter.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Needs {

    @Past
    private LocalDate lastWalk;
    @Past
    private LocalDate lastMeal;

    private boolean hasWater;

}

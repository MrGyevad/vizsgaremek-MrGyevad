package hu.progmasters.animalShelter.dto;

import hu.progmasters.animalShelter.domain.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class DogCommand {

    private String name;
    private int age;
    private Gender gender;
    private String breed;
    private boolean goneStray;
}

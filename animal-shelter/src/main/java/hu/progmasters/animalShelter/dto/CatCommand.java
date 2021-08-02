package hu.progmasters.animalShelter.dto;

import hu.progmasters.animalShelter.domain.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CatCommand {

    private String name;
    private int age;
    private Gender gender;
    private String breed;
    private Integer needsId;
    private Integer educationId;
}

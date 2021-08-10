package hu.progmasters.animalShelter.dto;

import hu.progmasters.animalShelter.domain.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
public class AnimalInfo {

    @Id
    public Integer id;
    private String name;
    private int age;
    private String breed;
    private Gender gender;
    private boolean hasWaterAndFood;
    private boolean adopted;
}

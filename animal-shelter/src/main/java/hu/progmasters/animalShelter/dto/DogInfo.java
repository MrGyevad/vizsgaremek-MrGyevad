package hu.progmasters.animalShelter.dto;

import hu.progmasters.animalShelter.domain.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
public class DogInfo {

    @Id
    private Integer id;
    private int age;
    private Gender gender;
    private String breed;
    private Integer needsId;
    private Integer educationId;
}
package hu.progmasters.animalShelter.dto;

import hu.progmasters.animalShelter.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalInfo {

    @Id
    public Integer id;
    private String name;
    private int age;
    private String breed;
    private Gender gender;
    LocalDateTime localDateTime;
    private boolean hasWaterAndFood;
    private boolean adopted;
}

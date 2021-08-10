package hu.progmasters.animalShelter.dto;

import hu.progmasters.animalShelter.domain.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DogInfo {

    @Id
    private Integer id;
    private String name;
    private int age;
    private String breed;
    private Gender gender;
    private LocalDateTime lastWalk;
    private boolean hasWaterAndFood;
    private boolean adopted;
}

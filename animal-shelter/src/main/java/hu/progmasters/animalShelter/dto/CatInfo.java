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
public class CatInfo {

    @Id
    private Integer id;
    private String name;
    private int age;
    private String breed;
    private Gender gender;
    private LocalDateTime lastPlay;
    private boolean hasWaterAndFood;
    private boolean adopted;
}

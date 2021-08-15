package hu.progmasters.animalShelter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hu.progmasters.animalShelter.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DogInfo {

    @Id
    private Integer id;
    private String name;
    private int age;
    private String breed;
    private Gender gender;
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime lastWalk;
    private boolean hasWaterAndFood;
    private boolean adopted;
}

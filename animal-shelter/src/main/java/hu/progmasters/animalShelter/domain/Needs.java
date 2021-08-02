package hu.progmasters.animalShelter.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Needs {

    @Id
    @GeneratedValue
    private Integer id;
    @Past
    private LocalDate lastWalk;
    @Past
    private LocalDate lastMeal;

    private boolean hasWater;

    @ManyToOne
    private Dog dog;

}

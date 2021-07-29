package hu.progmasters.animalShelter.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @PositiveOrZero
    private int age;
    // TODO @DogGenderValidation
    @Enumerated
    private Gender gender;

    private String breed;

    private Integer needsId;

    private Integer educationId;

    private boolean goneStray;
}

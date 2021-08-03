package hu.progmasters.animalShelter.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Dog extends Animal{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @PositiveOrZero
    private int age;

    private String breed;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @PastOrPresent
    private LocalDateTime lastWalk;

    private boolean hasWaterAndFood;

    private boolean goneStray;
}

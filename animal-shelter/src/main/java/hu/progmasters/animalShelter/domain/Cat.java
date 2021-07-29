package hu.progmasters.animalShelter.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int age;

    private String breed;
    //TODO @CatGender
    private Gender gender;

    private Integer needsId;

    private Integer educationId;

    private boolean goneStray;
}

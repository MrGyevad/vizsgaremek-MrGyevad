package hu.progmasters.animalShelter.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Cat implements Animal{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int age;

    private String breed;
    //TODO @CatGender
    private Gender gender;

    private Integer needsId;

    private Integer educationId;

    @OneToMany(mappedBy = "education")
    private List<Education> educations;

    private boolean goneStray;
}

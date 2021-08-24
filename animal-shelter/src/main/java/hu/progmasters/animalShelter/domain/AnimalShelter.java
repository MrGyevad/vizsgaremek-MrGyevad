package hu.progmasters.animalShelter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnimalShelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "animalShelter", fetch = FetchType.LAZY)
    private List<Cat> catList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "animalShelter", fetch = FetchType.LAZY)
    private List<Dog> dogList;
}

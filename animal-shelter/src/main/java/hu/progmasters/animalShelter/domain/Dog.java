package hu.progmasters.animalShelter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int age;
    private String breed;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDateTime lastWalk;
    private boolean hasWaterAndFood;
    private boolean adopted;
    @OneToOne(mappedBy = "dog", cascade = CascadeType.ALL)
    private BestFriend bestFriend;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AnimalShelter animalShelter;

    public AnimalShelter getAnimalShelter() {
        return animalShelter;
    }

    public void setAnimalShelter(AnimalShelter animalShelter) {
        this.animalShelter = animalShelter;
    }
}

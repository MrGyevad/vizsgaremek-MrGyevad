package hu.progmasters.animalShelter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @ToString.Exclude
    @OneToOne(mappedBy = "dog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BestFriend bestFriend;
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AnimalShelter animalShelter;
}

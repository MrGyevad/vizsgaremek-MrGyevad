package hu.progmasters.animalShelter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestFriend {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne(orphanRemoval = true,
    cascade = CascadeType.ALL)
    private Cat cat;

    @OneToOne(orphanRemoval = true,
            cascade = CascadeType.ALL)
    private Dog dog;
}

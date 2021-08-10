package hu.progmasters.animalShelter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestFriend {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    private Cat cat;

    @OneToOne
    private Dog dog;
}

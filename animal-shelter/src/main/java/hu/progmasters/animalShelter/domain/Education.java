package hu.progmasters.animalShelter.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean isHousebroken;

    private boolean walksWithLeash;

    private boolean sitsPawsLays;

    private boolean guardsTheHouse;

    @ManyToOne
    private Dog dog;

}

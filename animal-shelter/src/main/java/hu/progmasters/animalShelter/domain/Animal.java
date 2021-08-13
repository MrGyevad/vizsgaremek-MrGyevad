package hu.progmasters.animalShelter.domain;

import java.time.LocalDateTime;

public abstract class Animal {

    public Integer id;
    private String name;
    private int age;
    private Gender gender;
    private String breed;
    LocalDateTime localDateTime;
    private boolean hasWaterAndFood;
    private boolean adopted;


}

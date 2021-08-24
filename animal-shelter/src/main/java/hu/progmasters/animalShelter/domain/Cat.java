package hu.progmasters.animalShelter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int age;
    private String breed;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime lastPlay;
    private boolean hasWaterAndFood;
    private boolean adopted;
    @ToString.Exclude
    @OneToOne(mappedBy = "cat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BestFriend bestFriend;
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AnimalShelter animalShelter;
}

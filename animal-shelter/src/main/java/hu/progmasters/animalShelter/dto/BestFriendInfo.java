package hu.progmasters.animalShelter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestFriendInfo {

    @Id
    private Integer id;
    private CatInfo catInfo;
    private DogInfo dogInfo;
}

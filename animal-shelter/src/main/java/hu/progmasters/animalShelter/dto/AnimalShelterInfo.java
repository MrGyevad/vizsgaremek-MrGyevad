package hu.progmasters.animalShelter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalShelterInfo {

    @Id
    private Integer id;
    private String name;
    private List<CatInfo> catInfoList;
    private List<DogInfo> dogInfoList;
}

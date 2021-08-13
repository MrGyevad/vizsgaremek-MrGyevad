package hu.progmasters.animalShelter.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoBestFriendException extends RuntimeException{
    private String errorMessage = "Sadly, this animal has no best friend.";
    private Integer idOfLoneAnimal;
}

package hu.progmasters.animalShelter.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FriendShipNotFoundException extends RuntimeException {
    private String errorMessage = "Friendship not found";
}

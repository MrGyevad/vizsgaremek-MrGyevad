package hu.progmasters.animalShelter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationError>> handleValidationException(MethodArgumentNotValidException exception) {
        List<ValidationError> validationErrors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DogNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<AnimalShelterError> handleDogNotFoundException(DogNotFoundException exception){
        AnimalShelterError error = new AnimalShelterError();
        error.setMessage(exception.getMessage());
        error.setIdOfNotFound(exception.getIdOfNotFound());
        return List.of(error);

    }

    @ExceptionHandler(CatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<AnimalShelterError> handleCatNotFoundException(CatNotFoundException exception){
        AnimalShelterError error = new AnimalShelterError();
        error.setMessage(exception.getMessage());
        error.setIdOfNotFound(exception.getIdOfNotFound());
        return List.of(error);
    }

    @ExceptionHandler(NoBestFriendException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<AnimalShelterError> handleNoBestFriendException(NoBestFriendException exception){
        AnimalShelterError error = new AnimalShelterError();
        error.setMessage(exception.getMessage());
        error.setIdOfNotFound(exception.getIdOfLoneAnimal());
        return List.of(error);
    }

    @ExceptionHandler(FriendShipNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<AnimalShelterError> handleFriendShipNotFoundException(FriendShipNotFoundException exception){
        AnimalShelterError error = new AnimalShelterError();
        error.setMessage(exception.getMessage());
        return List.of(error);
    }

    @ExceptionHandler(AnimalShelterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<AnimalShelterError> handleAnimalShelterNotFoundException(AnimalShelterNotFoundException exception){
        AnimalShelterError error = new AnimalShelterError();
        error.setMessage(exception.getMessage());
        error.setIdOfNotFound(exception.getIdOfNotFound());
        return List.of(error);
    }
}

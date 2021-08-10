package hu.progmasters.animalShelter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
    }/*

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<NotLegitTimeException>> handleNotLegitTimeException(MethodArgumentNotValidException exception){
        List<NotLegitTimeException> notLegitTimeExceptions = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new NotLegitTimeException(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(notLegitTimeExceptions, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DogNotFoundException>> handleDogNotFoundException(MethodArgumentNotValidException exception){
        List<DogNotFoundException> dogNotFoundExceptions = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new DogNotFoundException(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(dogNotFoundExceptions, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<CatNotFoundException>> handleCatNotFoundException(MethodArgumentNotValidException exception){
        List<CatNotFoundException> catNotFoundExceptions = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new CatNotFoundException(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(catNotFoundExceptions, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<NoBestFriendException>> handleNoBestFriendException(MethodArgumentNotValidException exception){
        List<NoBestFriendException> noBestFriendExceptions = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new NoBestFriendException(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(noBestFriendExceptions, HttpStatus.NOT_FOUND);
    }*/
}

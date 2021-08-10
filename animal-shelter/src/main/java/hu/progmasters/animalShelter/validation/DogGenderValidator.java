package hu.progmasters.animalShelter.validation;

import hu.progmasters.animalShelter.domain.Gender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class DogGenderValidator implements ConstraintValidator<DogGender, Gender> {

    private final List<Gender> dogGenders = List.of(Gender.SIRE, Gender.BITCH);

    @Override
    public boolean isValid(Gender gender, ConstraintValidatorContext constraintValidatorContext) {
        return dogGenders.contains(gender);
    }
}
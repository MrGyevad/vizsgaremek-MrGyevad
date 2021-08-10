package hu.progmasters.animalShelter.validation;

import hu.progmasters.animalShelter.domain.Gender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class CatGenderValidator implements ConstraintValidator<CatGender, Gender> {

    private final List<Gender> catGenders = List.of(Gender.TOM, Gender.PUSSY);

    @Override
    public boolean isValid(Gender gender, ConstraintValidatorContext constraintValidatorContext) {
        return catGenders.contains(gender);
    }
}

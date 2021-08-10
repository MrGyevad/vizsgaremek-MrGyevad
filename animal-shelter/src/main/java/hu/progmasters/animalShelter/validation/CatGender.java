package hu.progmasters.animalShelter.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CatGenderValidator.class)
public @interface CatGender {

    String message() default "Gender must be TOM, or PUSSY.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package dev.klimkin.creditconveyor.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AgeValidator implements ConstraintValidator<AgeValidation, LocalDate> {
    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext cxt) {
        return isAlreadyEighteen(dateOfBirth);
    }

    private boolean isAlreadyEighteen(LocalDate dateOfBirth) {

        LocalDate now = LocalDate.now();
        LocalDate minAge = now.minusYears(18);

        boolean result = false;

        try{
            result = dateOfBirth.isBefore(minAge);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}

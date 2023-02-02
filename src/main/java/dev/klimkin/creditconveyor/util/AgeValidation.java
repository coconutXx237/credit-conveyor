package dev.klimkin.creditconveyor.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AgeValidator.class)
public @interface AgeValidation {

    String message() default "Invalid age, must be 18 years old or greater";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

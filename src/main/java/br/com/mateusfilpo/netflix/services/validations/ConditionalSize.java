package br.com.mateusfilpo.netflix.services.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ConditionalSizeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalSize {
    String message() default "The list must have at least 3 elements if it is not null.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int min() default 3;
}
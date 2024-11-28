package br.com.mateusfilpo.netflix.services.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueUsernameUpdateValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsernameUpdate {
    String message() default "Username already exists.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

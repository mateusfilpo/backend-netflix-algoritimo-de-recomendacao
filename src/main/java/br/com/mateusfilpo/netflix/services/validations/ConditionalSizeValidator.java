package br.com.mateusfilpo.netflix.services.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ConditionalSizeValidator implements ConstraintValidator<ConditionalSize, List<?>> {

    private int min;

    @Override
    public void initialize(ConditionalSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(List<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.size() >= min;
    }
}
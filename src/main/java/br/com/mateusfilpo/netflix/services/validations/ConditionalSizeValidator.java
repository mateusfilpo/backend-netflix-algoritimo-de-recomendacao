package br.com.mateusfilpo.netflix.services.validations;

import br.com.mateusfilpo.netflix.dtos.UserUpdateDTO;
import br.com.mateusfilpo.netflix.dtos.MovieUpdateDTO;
import br.com.mateusfilpo.netflix.dtos.UserGenreDTO;
import br.com.mateusfilpo.netflix.dtos.MovieGenreDTO;
import br.com.mateusfilpo.netflix.dtos.errors.FieldMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class ConditionalSizeValidator implements ConstraintValidator<ConditionalSize, Object> {

    private int min;

    @Override
    public void initialize(ConditionalSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto instanceof UserUpdateDTO) {
            return validateUserUpdateDTO((UserUpdateDTO) dto, context);
        } else if (dto instanceof MovieUpdateDTO) {
            return validateMovieUpdateDTO((MovieUpdateDTO) dto, context);
        }
        return true;
    }

    private boolean validateUserUpdateDTO(UserUpdateDTO dto, ConstraintValidatorContext context) {
        return validateGenres(dto.getGenres(), context);
    }

    private boolean validateMovieUpdateDTO(MovieUpdateDTO dto, ConstraintValidatorContext context) {
        return validateGenres(dto.getGenres(), context);
    }

    private boolean validateGenres(List<?> genres, ConstraintValidatorContext context) {
        if (genres == null) {
            return true;
        }

        List<FieldMessage> list = new ArrayList<>();
        if (genres.size() < min) {
            list.add(new FieldMessage("genres", "The genres list must have at least " + min + " items if it is not null."));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }
}

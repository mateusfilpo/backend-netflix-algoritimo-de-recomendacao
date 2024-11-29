package br.com.mateusfilpo.netflix.services.validations;

import br.com.mateusfilpo.netflix.dtos.GenreDTO;
import br.com.mateusfilpo.netflix.dtos.errors.FieldMessage;
import br.com.mateusfilpo.netflix.repositories.GenreRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UniqueGenreValidator implements ConstraintValidator<UniqueGenre, GenreDTO> {

    private final GenreRepository genreRepository;

    public UniqueGenreValidator(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public void initialize(UniqueGenre ann) {
    }

    @Override
    public boolean isValid(GenreDTO dto, ConstraintValidatorContext context) {

        if (dto.getName() == null) {
            return true;
        }

        List<FieldMessage> list = new ArrayList<>();

        if (!StringUtils.hasText(dto.getName())) {
            list.add(new FieldMessage("name", "Name cannot be blank."));
        } else if (genreRepository.existsByName(dto.getName())) {
            list.add(new FieldMessage("name", "This genre already exists."));
        }



        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }
}

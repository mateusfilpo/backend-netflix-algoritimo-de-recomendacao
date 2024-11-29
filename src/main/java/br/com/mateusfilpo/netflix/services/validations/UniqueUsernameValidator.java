package br.com.mateusfilpo.netflix.services.validations;

import br.com.mateusfilpo.netflix.dtos.users.UserCreateDTO;
import br.com.mateusfilpo.netflix.dtos.errors.FieldMessage;
import br.com.mateusfilpo.netflix.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, UserCreateDTO> {

    private final UserRepository userRepository;

    public UniqueUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(UserCreateDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        if (userRepository.existsByUsername(dto.getUsername())) {
            list.add(new FieldMessage("username", "This username is already taken."));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }
}

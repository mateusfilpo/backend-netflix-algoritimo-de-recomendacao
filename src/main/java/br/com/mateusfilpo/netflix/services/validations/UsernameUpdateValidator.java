package br.com.mateusfilpo.netflix.services.validations;

import br.com.mateusfilpo.netflix.dtos.UserUpdateDTO;
import br.com.mateusfilpo.netflix.dtos.errors.FieldMessage;
import br.com.mateusfilpo.netflix.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UsernameUpdateValidator implements ConstraintValidator<UsernameUpdate, UserUpdateDTO> {

    private final UserRepository userRepository;

    public UsernameUpdateValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
        if (dto.getUsername() == null) {
            return true;
        }

        List<FieldMessage> list = new ArrayList<>();

        if (!StringUtils.hasText(dto.getUsername())) {
            list.add(new FieldMessage("username", "Username cannot be blank."));
        } else if (userRepository.existsByUsername(dto.getUsername())) {
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

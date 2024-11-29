package br.com.mateusfilpo.netflix.services.validations;

import br.com.mateusfilpo.netflix.dtos.users.UserUpdateDTO;
import br.com.mateusfilpo.netflix.dtos.errors.FieldMessage;
import br.com.mateusfilpo.netflix.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EmailUpdateValidator implements ConstraintValidator<EmailUpdate, UserUpdateDTO> {

    private final UserRepository userRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    public EmailUpdateValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        if (dto.getEmail() == null) {
            return true;
        }

        List<FieldMessage> list = new ArrayList<>();

        if (!StringUtils.hasText(dto.getEmail())) {
            list.add(new FieldMessage("email", "Email cannot be blank."));
        } else if (!EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
            list.add(new FieldMessage("email", "Email is invalid"));
        } else if (userRepository.existsByEmail(dto.getEmail())) {
            list.add(new FieldMessage("email", "This email is already taken."));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }

}

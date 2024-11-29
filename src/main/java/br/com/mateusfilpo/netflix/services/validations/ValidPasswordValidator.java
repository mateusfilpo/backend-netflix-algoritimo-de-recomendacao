package br.com.mateusfilpo.netflix.services.validations;

import br.com.mateusfilpo.netflix.dtos.users.UserCreateDTO;
import br.com.mateusfilpo.netflix.dtos.users.UserUpdateDTO;
import br.com.mateusfilpo.netflix.dtos.errors.FieldMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto instanceof UserCreateDTO) {
            return validateUserCreateDTO((UserCreateDTO) dto, context);
        } else if (dto instanceof UserUpdateDTO) {
            return validateUserUpdateDTO((UserUpdateDTO) dto, context);
        }
        return true;
    }

    private boolean validateUserCreateDTO(UserCreateDTO dto, ConstraintValidatorContext context) {
        return validatePassword(dto.getPassword(), context);
    }

    private boolean validateUserUpdateDTO(UserUpdateDTO dto, ConstraintValidatorContext context) {
        return validatePassword(dto.getPassword(), context);
    }

    private boolean validatePassword(String password, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        if (password == null || !pattern.matcher(password).matches()) {
            list.add(new FieldMessage("password", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character."));
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

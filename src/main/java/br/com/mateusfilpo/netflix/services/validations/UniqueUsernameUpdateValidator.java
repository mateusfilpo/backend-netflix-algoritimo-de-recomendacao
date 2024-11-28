package br.com.mateusfilpo.netflix.services.validations;

import br.com.mateusfilpo.netflix.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameUpdateValidator implements ConstraintValidator<UniqueUsernameUpdate, String> {

    private final UserRepository userRepository;

    public UniqueUsernameUpdateValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return true;
        }
        return !userRepository.existsByUsername(username);
    }

}

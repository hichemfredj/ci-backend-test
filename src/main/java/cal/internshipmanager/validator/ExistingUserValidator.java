package cal.internshipmanager.validator;

import cal.internshipmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@Component
public class ExistingUserValidator implements ConstraintValidator<ExistingUser, UUID> {

    //
    // Dependencies
    //

    private final UserRepository userRepository;

    //
    // Constructors
    //

    @Autowired
    public ExistingUserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //
    // Validation
    //

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.findById(uuid).isPresent();
    }

}

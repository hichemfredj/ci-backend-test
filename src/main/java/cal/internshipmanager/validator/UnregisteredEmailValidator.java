package cal.internshipmanager.validator;

import cal.internshipmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UnregisteredEmailValidator implements ConstraintValidator<UnregisteredEmail, String> {

    //
    // Dependencies
    //

    private final UserRepository userRepository;

    //
    // Constructors
    //

    @Autowired
    public UnregisteredEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //
    // Validation
    //

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.findByEmail(s).isEmpty();
    }

}

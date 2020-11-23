package cal.internshipmanager.validator;

import cal.internshipmanager.repository.InternshipApplicationRepository;
import cal.internshipmanager.repository.InternshipOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@Component
public class ExistingInternshipApplicationValidator implements ConstraintValidator<ExistingInternshipApplication, UUID> {

    //
    // Dependencies
    //

    private final InternshipApplicationRepository internshipApplicationRepository;

    //
    // Constructors
    //

    @Autowired
    public ExistingInternshipApplicationValidator(InternshipApplicationRepository internshipApplicationRepository) {
        this.internshipApplicationRepository = internshipApplicationRepository;
    }

    //
    // Validation
    //

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        return internshipApplicationRepository.findById(uuid).isPresent();
    }

}

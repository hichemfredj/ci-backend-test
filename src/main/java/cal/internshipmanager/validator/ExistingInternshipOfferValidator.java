package cal.internshipmanager.validator;

import cal.internshipmanager.repository.InternshipOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@Component
public class ExistingInternshipOfferValidator implements ConstraintValidator<ExistingInternshipOffer, UUID> {

    //
    // Dependencies
    //

    private final InternshipOfferRepository internshipOfferRepository;

    //
    // Constructors
    //

    @Autowired
    public ExistingInternshipOfferValidator(InternshipOfferRepository internshipOfferRepository) {
        this.internshipOfferRepository = internshipOfferRepository;
    }

    //
    // Validation
    //

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        return internshipOfferRepository.findById(uuid).isPresent();
    }

}

package cal.internshipmanager.validator;

import cal.internshipmanager.repository.InternshipOfferRepository;
import cal.internshipmanager.repository.PortfolioDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@Component
public class ExistingPortfolioDocumentValidator implements ConstraintValidator<ExistingPortfolioDocument, UUID> {

    //
    // Dependencies
    //

    private final PortfolioDocumentRepository portfolioDocumentRepository;

    //
    // Constructors
    //

    @Autowired
    public ExistingPortfolioDocumentValidator(PortfolioDocumentRepository portfolioDocumentRepository) {
        this.portfolioDocumentRepository = portfolioDocumentRepository;
    }

    //
    // Validation
    //

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        return portfolioDocumentRepository.findById(uuid).isPresent();
    }

}

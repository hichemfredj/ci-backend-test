package cal.internshipmanager.validator;

import cal.internshipmanager.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@Component
public class ExistingContractValidator implements ConstraintValidator<ExistingContract, UUID> {

    //
    // Dependencies
    //

    private final ContractRepository contractRepository;

    //
    // Constructors
    //

    @Autowired
    public ExistingContractValidator(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    //
    // Validation
    //

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        return contractRepository.findById(uuid).isPresent();
    }

}

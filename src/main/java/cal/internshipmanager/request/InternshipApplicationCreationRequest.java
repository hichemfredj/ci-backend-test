package cal.internshipmanager.request;

import cal.internshipmanager.validator.ExistingInternshipOffer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class InternshipApplicationCreationRequest implements Serializable {

    @NotNull(message = "Offer unique id is mandatory")
    @ExistingInternshipOffer(message = "Internship offer does not exist")
    private UUID offerUniqueId;

    @NotNull(message = "Documents are mandatory")
    private List<UUID> documents;
}

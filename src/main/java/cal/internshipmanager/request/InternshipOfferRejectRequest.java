package cal.internshipmanager.request;

import cal.internshipmanager.validator.ExistingInternshipOffer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
public class InternshipOfferRejectRequest implements Serializable {

    @NotNull(message = "Unique id is mandatory")
    @ExistingInternshipOffer(message = "Internship offer does not exist")
    private UUID uniqueId;

}

package cal.internshipmanager.request;

import cal.internshipmanager.validator.ExistingInternshipOffer;
import cal.internshipmanager.validator.ExistingUser;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
public class InternshipOfferRemoveUserRequest implements Serializable {

    @NotNull(message = "Offer unique id is mandatory")
    @ExistingInternshipOffer(message = "Internship offer does not exist")
    private UUID offerUniqueId;

    @NotNull(message = "User unique id is mandatory")
    @ExistingUser(message = "User does not exist")
    private UUID userUniqueId;

}

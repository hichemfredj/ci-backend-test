package cal.internshipmanager.request;

import cal.internshipmanager.validator.ExistingUser;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Data
public class SendNotificationRequest implements Serializable {

    @ExistingUser
    private UUID userUniqueId;

    @NotBlank
    private String type;

    @NotBlank
    private String message;
}

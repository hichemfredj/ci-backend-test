package cal.internshipmanager.response;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class AuthenticationResponse implements Serializable {

    //
    // Fields
    //

    private UUID userUniqueId;

    private String userType;

    private String token;

}

package cal.internshipmanager.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class AuthenticationRequest implements Serializable {

    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

}

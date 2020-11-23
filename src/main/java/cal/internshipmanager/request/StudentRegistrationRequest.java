package cal.internshipmanager.request;

import cal.internshipmanager.validator.UnregisteredEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class StudentRegistrationRequest implements Serializable {

    @Email(message = "Email is not valid")
    @UnregisteredEmail(message = "Email already in use")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 18, message = "Password size must be between 6 and 18")
    private String password;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "First name is mandatory")
    private String lastName;

}

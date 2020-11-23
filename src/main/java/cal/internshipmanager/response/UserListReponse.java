package cal.internshipmanager.response;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class UserListReponse implements Serializable {

    //
    // Fields
    //

    private List<UserListReponse.User> users;


    //
    // Inner classes & Enums
    //

    @Data
    public static class User {

        @Id
        private UUID uniqueId;

        private String type;

        private String email;

        private String firstName;

        private String lastName;

        private String company;

    }

    //
    // Utils
    //

    public static UserListReponse.User map(cal.internshipmanager.model.User from) {

        if (from == null)
            return null;

        UserListReponse.User user = new UserListReponse.User();

        user.uniqueId = from.getUniqueId();
        user.type = from.getType().toString();
        user.email = from.getEmail();
        user.firstName = from.getFirstName();
        user.lastName = from.getLastName();
        user.company = from.getCompany();

        return user;
    }

}

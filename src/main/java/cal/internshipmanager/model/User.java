package cal.internshipmanager.model;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Document
@Data
public class User implements Serializable {

    //
    // Fields
    //

    @Id
    private UUID uniqueId;

    private Type type;

    private String email;

    private String passwordHash;

    private String firstName;

    private String lastName;

    private String company;

    private Signature signature;
    
    private List<Notification> notifications;

    //
    // Inner classes & Enums
    //

    public enum Type {
        STUDENT, EMPLOYER, ADMINISTRATOR;
    }

}

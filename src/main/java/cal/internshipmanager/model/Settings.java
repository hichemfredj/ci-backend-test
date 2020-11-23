package cal.internshipmanager.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Settings {

    //
    // Fields
    //

    @Id
    private long creationTimestamp;

    private String semester;

    private Boolean requireApproval;

}

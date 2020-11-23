package cal.internshipmanager.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Notification implements Serializable {

    //
    // Fields
    //

    private Date date;

    private String type;

    private String message;
}

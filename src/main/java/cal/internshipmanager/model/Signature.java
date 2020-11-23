package cal.internshipmanager.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
public class Signature implements Serializable {

    //
    // Fields
    //

    private UUID uniqueId;

    private Date uploadDate;

    private byte[] data;
}

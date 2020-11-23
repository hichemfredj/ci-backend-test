package cal.internshipmanager.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Document
@Data
public class PortfolioDocument implements Serializable {

    //
    // Fields
    //

    @Id
    private UUID uniqueId;

    private UUID userUniqueId;

    private String fileName;

    private String fileType;

    private String type;

    private Date uploadDate;

    private Boolean approved;

    private byte[] data;

}

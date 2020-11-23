package cal.internshipmanager.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Document
@Data
public class InternshipApplication implements Serializable {

    //
    // Fields
    //

    @Id
    private UUID uniqueId;

    private String semester;

    private User student;

    private InternshipOffer offer;

    //private UUID studentUniqueId;

    //private UUID offerUniqueId;

    private Date date;

    private Date interviewDate;

    private List<PortfolioDocument> documents;

    private Status status;

    public enum Status {
        PENDING_APPROVAL, APPROVED, REJECTED, SELECTED;
    }

}

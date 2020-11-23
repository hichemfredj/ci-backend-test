package cal.internshipmanager.response;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class SignatureResponse {

    private UUID uniqueId;

    private Date uploadDate;

}

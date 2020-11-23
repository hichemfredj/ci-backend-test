package cal.internshipmanager.request;

import cal.internshipmanager.validator.ExistingPortfolioDocument;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
public class PortfolioDocumentDeleteRequest implements Serializable {

    @NotNull(message = "Portfolio document unique id is mandatory")
    @ExistingPortfolioDocument(message = "Portfolio document does not exist")
    private UUID uniqueId;

}

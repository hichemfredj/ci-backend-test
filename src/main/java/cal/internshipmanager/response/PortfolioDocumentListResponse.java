package cal.internshipmanager.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class PortfolioDocumentListResponse implements Serializable {

    //
    // Fields
    //

    private List<PortfolioDocument> portfolioDocuments;


    //
    // Inner classes & Enums
    //

    @Data
    public static class PortfolioDocument {

        private UUID uniqueId;

        private String fileName;

        private String fileType;

        private String type;

        private boolean approved;

        private long uploadDate;

    }

    //
    // Utils
    //

    public static PortfolioDocument map(cal.internshipmanager.model.PortfolioDocument from) {

        PortfolioDocument portfolioDocument = new PortfolioDocument();

        portfolioDocument.uniqueId = from.getUniqueId();
        portfolioDocument.fileName = from.getFileName();
        portfolioDocument.fileType = from.getFileType();
        portfolioDocument.type = from.getType();
        portfolioDocument.approved = from.getApproved();
        portfolioDocument.uploadDate = from.getUploadDate().getTime();

        return portfolioDocument;
    }

}

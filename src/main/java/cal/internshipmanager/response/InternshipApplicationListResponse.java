package cal.internshipmanager.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class InternshipApplicationListResponse implements Serializable {

    //
    // Fields
    //

    private List<InternshipApplication> applications;

    //
    // Inner classes & Enums
    //

    @Data
    public static class InternshipApplication {

        private UUID uniqueId;

        private UUID studentUniqueId;

        private String studentFirstName;

        private String studentLastName;

        private UUID offerUniqueId;

        private String company;

        private String jobTitle;    

        private Long date;

        private Long interviewDate;

        private String status;

    }

    //
    // Utils
    //

    public static InternshipApplication map(cal.internshipmanager.model.InternshipApplication from) {

        if (from == null)
            return null;

        InternshipApplication application = new InternshipApplication();

        application.uniqueId = from.getUniqueId();
        application.studentUniqueId = from.getStudent().getUniqueId();
        application.studentFirstName = from.getStudent().getFirstName();
        application.studentLastName = from.getStudent().getLastName();
        application.offerUniqueId = from.getOffer().getUniqueId();
        application.company = from.getOffer().getCompany();
        application.jobTitle = from.getOffer().getJobTitle();
        application.date = from.getDate().getTime();
        application.interviewDate = from.getInterviewDate().getTime();
        application.status = from.getStatus().toString();

        return application;
    }

}

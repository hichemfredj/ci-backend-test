package cal.internshipmanager.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class InternshipOfferListResponse implements Serializable {

    //
    // Fields
    //

    private List<InternshipOffer> internshipOffers;


    //
    // Inner classes & Enums
    //

    @Data
    public static class InternshipOffer {

        private UUID uniqueId;

        private UUID employer;

        private String status;

        private String company;

        private String jobTitle;

        private List<String> jobScope;

        private Long startDate;

        private Long endDate;

        private String location;

        private float salary;

        private float hours;

    }

    //
    // Utils
    //

    public static InternshipOffer map(cal.internshipmanager.model.InternshipOffer from) {

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.uniqueId = from.getUniqueId();
        internshipOffer.employer = from.getEmployer();
        internshipOffer.status = from.getStatus().toString();
        internshipOffer.company = from.getCompany();
        internshipOffer.jobTitle = from.getJobTitle();
        internshipOffer.jobScope = from.getJobScope();
        internshipOffer.startDate = from.getStartDate().getTime();
        internshipOffer.endDate = from.getEndDate().getTime();
        internshipOffer.location = from.getLocation();
        internshipOffer.salary = from.getSalary();
        internshipOffer.hours = from.getHours();

        return internshipOffer;
    }

}

package cal.internshipmanager.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class InternshipOfferCreationRequest implements Serializable {

    @NotBlank(message = "Company is mandatory")
    private String company;

    @NotBlank(message = "Job title is mandatory")
    private String jobTitle;

    @NotNull(message = "Job scope is mandatory")
    private List<String> jobScope;

    @NotNull(message = "Start date is mandatory")
    private Long startDate;

    @NotNull(message = "End date is mandatory")
    private Long endDate;

    @NotBlank(message = "Location is mandatory")
    private String location;

    @Min(value = 0, message = "Salary must be at least 0.00$")
    private float salary;

    @Min(value = 1, message = "Hours must be at least 1 hour")
    private float hours;

}

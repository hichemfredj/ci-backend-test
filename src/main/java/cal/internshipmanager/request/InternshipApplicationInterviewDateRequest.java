package cal.internshipmanager.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternshipApplicationInterviewDateRequest implements Serializable {

    private Long interviewDate;

}

package cal.internshipmanager.response;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class ContractListResponse {

    //
    // Fields
    //

    private List<Contract> contracts;

    //
    // Inner classes & Enums
    //

    @Data
    public static class Contract {

        private UUID uniqueId;

        private String semester;

        private String status;

        private InternshipApplicationListResponse.InternshipApplication application;

        private UserListReponse.User administrator;

        private long creationDate;

        private Signature studentSignature;

        private Signature employerSignature;

        private Signature administratorSignature;

        private long studentSignedDate;

        private long employerSignedDate;

        private long administratorSignedDate;

        private UUID currentUserUniqueId;

    }

    @Data
    public static class Signature {

        private UUID uniqueId;

        private long uploadDate;

    }

    //
    // Utils
    //

    public static Contract map(cal.internshipmanager.model.Contract from) {

        Contract contract = new Contract();

        contract.uniqueId = from.getUniqueId();
        contract.semester = from.getSemester();
        contract.status = from.getStatus().toString();
        contract.application = InternshipApplicationListResponse.map(from.getApplication());
        contract.administrator = UserListReponse.map(from.getAdministrator());
        contract.creationDate = from.getCreationDate().getTime();
        contract.studentSignature = map(from.getStudentSignature());
        contract.employerSignature = map(from.getEmployerSignature());
        contract.administratorSignature = map(from.getAdministratorSignature());
        contract.studentSignedDate = from.getStudentSignedDate() == null ? -1 : from.getStudentSignedDate().getTime();
        contract.employerSignedDate = from.getEmployerSignedDate() == null ? -1 :from.getEmployerSignedDate().getTime();
        contract.administratorSignedDate = from.getAdministratorSignedDate() == null ? -1 :from.getAdministratorSignedDate().getTime();
        contract.currentUserUniqueId = from.getCurrentUserUniqueId();

        return contract;
    }

    public static Signature map(cal.internshipmanager.model.Signature from) {
        if (from == null)
            return null;

        Signature signature = new Signature();

        signature.uniqueId = from.getUniqueId();
        signature.uploadDate = from.getUploadDate().getTime();

        return signature;
    }

}

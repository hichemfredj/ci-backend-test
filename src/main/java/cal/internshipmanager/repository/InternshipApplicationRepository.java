package cal.internshipmanager.repository;

import cal.internshipmanager.model.InternshipApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InternshipApplicationRepository extends MongoRepository<InternshipApplication, UUID> {

    List<InternshipApplication> findAllBySemester(String semester);

    List<InternshipApplication> findAllByStudentUniqueIdAndSemester(UUID studentUniqueId, String semester);

    List<InternshipApplication> findAllByStatusAndSemester(InternshipApplication.Status status, String semester);

    List<InternshipApplication> findAllByOfferUniqueIdAndStatusAndSemester(UUID offerUniqueId, InternshipApplication.Status status, String semester);

}

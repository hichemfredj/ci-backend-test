package cal.internshipmanager.repository;

import cal.internshipmanager.model.InternshipOffer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InternshipOfferRepository extends MongoRepository<InternshipOffer, UUID> {

    List<InternshipOffer> findAllBySemester(String semester);

    List<InternshipOffer> findAllByStatusAndSemester(InternshipOffer.Status status, String semester);

    List<InternshipOffer> findAllByEmployerAndStatusAndSemester(UUID uniqueId, InternshipOffer.Status status, String semester);

}

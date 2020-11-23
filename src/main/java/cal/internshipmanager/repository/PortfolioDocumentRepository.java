package cal.internshipmanager.repository;

import cal.internshipmanager.model.PortfolioDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PortfolioDocumentRepository extends MongoRepository<PortfolioDocument, UUID> {

    List<PortfolioDocument> findAllByUserUniqueId(UUID userUniqueId);

    List<PortfolioDocument> findAllByUserUniqueIdAndApproved(UUID userUniqueId, Boolean approved);

}

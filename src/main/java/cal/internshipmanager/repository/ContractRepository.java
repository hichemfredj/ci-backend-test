package cal.internshipmanager.repository;

import cal.internshipmanager.model.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContractRepository extends MongoRepository<Contract, UUID> {

    List<Contract> findAllBySemester(String semester);

    List<Contract> findAllBySemesterAndStatus(String semester, Contract.Status status);

    List<Contract> findAllBySemesterAndCurrentUserUniqueId(String Semester, UUID currentUserUniqueId);
}

package cal.internshipmanager.repository;

import cal.internshipmanager.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    List<User> findAllByType(User.Type type);

}

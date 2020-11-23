package cal.internshipmanager.repository;

import cal.internshipmanager.model.Settings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends MongoRepository<Settings, Long> {

}

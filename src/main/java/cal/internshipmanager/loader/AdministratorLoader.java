package cal.internshipmanager.loader;

import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Order(2)
@Component
public class AdministratorLoader implements CommandLineRunner {

    //
    // Dependencies
    //

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    //
    // Constructors
    //

    @Autowired
    public AdministratorLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //
    // Services
    //

    @Override
    public void run(String... args) {

        LoadIfAbsent("admin@admin.com", "Admin", "Admin", "123456");

    }

    //
    // Private Methods
    //

    private void LoadIfAbsent(String email, String firstName, String lastName, String password) {
        if (userRepository.findByEmail(email).isEmpty()) {

            User user = new User();

            user.setUniqueId(UUID.randomUUID());
            user.setType(User.Type.ADMINISTRATOR);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPasswordHash(passwordEncoder.encode(password));

            userRepository.save(user);
        }
    }

}

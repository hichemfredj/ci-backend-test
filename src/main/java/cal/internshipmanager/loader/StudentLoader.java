package cal.internshipmanager.loader;

import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Order(3)
@Component
public class StudentLoader implements CommandLineRunner {

    //
    // Constants
    //

    private static final int MIN_STUDENT_AMOUNT = 20;

    private static final String PASSWORD = "123456";

    //
    // Dependencies
    //

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    //
    // Constructors
    //

    @Autowired
    public StudentLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //
    // Services
    //

    @Override
    public void run(String... args) {

        final int needed = MIN_STUDENT_AMOUNT -  userRepository.findAllByType(User.Type.STUDENT).size();

        for(int i = 0; i < needed; i++){

            final User user = generate();

            userRepository.save(user);
        }

    }

    //
    // Private Methods
    //

    private User generate() {

        final Faker faker = new Faker();

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        String email = firstName + "_" + lastName + "@faker.com";

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.STUDENT);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPasswordHash(passwordEncoder.encode(PASSWORD));

        return user;
    }

}

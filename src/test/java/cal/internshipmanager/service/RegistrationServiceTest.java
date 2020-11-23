package cal.internshipmanager.service;

import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.request.EmployerRegistrationRequest;
import cal.internshipmanager.request.StudentRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void student_validRequest() {

        // Arrange

        RegistrationService service = new RegistrationService(userRepository, passwordEncoder);

        StudentRegistrationRequest request = new StudentRegistrationRequest();

        request.setEmail("test@test.com");
        request.setFirstName("test");
        request.setLastName("test");
        request.setPassword("123456789");

        // Act & Assert

        when(userRepository.save(any())).then(inv -> {

            User user = inv.getArgument(0);

            assertNotNull(user.getUniqueId());
            assertEquals(request.getEmail(), user.getEmail());
            assertEquals(request.getFirstName(), user.getFirstName());
            assertEquals(request.getLastName(), user.getLastName());
            assertEquals(User.Type.STUDENT, user.getType());
            assertTrue(passwordEncoder.matches(request.getPassword(), user.getPasswordHash()));

            return null;
        });

        service.student(request);
    }

    @Test
    public void employer_validRequest() {

        // Arrange

        RegistrationService service = new RegistrationService(userRepository, passwordEncoder);

        EmployerRegistrationRequest request = new EmployerRegistrationRequest();

        request.setEmail("test@test.com");
        request.setFirstName("test");
        request.setLastName("test");
        request.setPassword("123456789");
        request.setCompany("TestCorp");

        // Act & Assert

        when(userRepository.save(any())).then(inv -> {

            User user = inv.getArgument(0);

            assertEquals(request.getEmail(), user.getEmail());
            assertEquals(request.getFirstName(), user.getFirstName());
            assertEquals(request.getLastName(), user.getLastName());
            assertEquals(User.Type.EMPLOYER, user.getType());
            assertEquals(request.getCompany(), user.getCompany());
            assertTrue(passwordEncoder.matches(request.getPassword(), user.getPasswordHash()));

            return null;
        });

        service.employer(request);
    }

}

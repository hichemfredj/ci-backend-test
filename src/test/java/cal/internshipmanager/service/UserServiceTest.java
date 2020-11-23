package cal.internshipmanager.service;

import cal.internshipmanager.model.InternshipApplication;
import cal.internshipmanager.model.InternshipOffer;
import cal.internshipmanager.model.Settings;
import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.InternshipApplicationRepository;
import cal.internshipmanager.repository.InternshipOfferRepository;
import cal.internshipmanager.repository.SettingsRepository;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.response.UserListReponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InternshipApplicationRepository internshipApplicationRepository;

    @Mock
    private InternshipOfferRepository internshipOfferRepository;

    @Mock
    private SettingsRepository settingsRepository;

    @Test
    public void studentUsers_validRequest() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.STUDENT);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");

        UserService userService = new UserService(userRepository, internshipApplicationRepository,internshipOfferRepository, new SettingsService(settingsRepository));

        when(userRepository.findAllByType(User.Type.STUDENT))
                .thenReturn(List.of(user));

        // Act

        UserListReponse response = userService.students();

        // Assert

        for (UserListReponse.User user1 : response.getUsers()) {

            assertEquals(user.getUniqueId(), user1.getUniqueId());
            assertEquals(user.getType().toString(), user1.getType());
            assertEquals(user.getEmail(), user1.getEmail());
            assertEquals(user.getFirstName(), user1.getFirstName());
            assertEquals(user.getLastName(), user1.getLastName());
            assertEquals(user.getCompany(), user1.getCompany());

        }
    }

    @Test
    public void studentsWithApplication_validRequest() {
        // Arrange

        Settings settings = new Settings();
        settings.setCreationTimestamp(new Date().getTime());
        settings.setSemester("2020-03-16");

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.STUDENT);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");

        InternshipApplication internshipApplication = new InternshipApplication();

        internshipApplication.setUniqueId(UUID.randomUUID());
        internshipApplication.setDate(new Date());
        internshipApplication.setStatus(InternshipApplication.Status.SELECTED);

        UserService userService = new UserService(userRepository, internshipApplicationRepository,internshipOfferRepository, new SettingsService(settingsRepository));

        when(userRepository.findAllByType(User.Type.STUDENT))
                .thenReturn(List.of(user));

        when(internshipApplicationRepository.findAllByStudentUniqueIdAndSemester(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(internshipApplication));

        when(settingsRepository.findAll()).thenReturn(List.of(settings));

        // Act

        UserListReponse response = userService.studentsWithoutApplication();

        // Assert

        for (UserListReponse.User user1 : response.getUsers()) {

            assertEquals(user.getUniqueId(), user1.getUniqueId());
            assertEquals(user.getType().toString(), user1.getType());
            assertEquals(user.getEmail(), user1.getEmail());
            assertEquals(user.getFirstName(), user1.getFirstName());
            assertEquals(user.getLastName(), user1.getLastName());
            assertEquals(user.getCompany(), user1.getCompany());

        }
    }

    @Test
    public void studentsWithoutApplication_validRequest() {
        // Arrange

        Settings settings = new Settings();
        settings.setCreationTimestamp(new Date().getTime());
        settings.setSemester("2020-03-16");

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.STUDENT);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");

        InternshipApplication internshipApplication = new InternshipApplication();

        internshipApplication.setUniqueId(UUID.randomUUID());
        internshipApplication.setDate(new Date());
        internshipApplication.setStatus(InternshipApplication.Status.SELECTED);

        UserService userService = new UserService(userRepository, internshipApplicationRepository,internshipOfferRepository, new SettingsService(settingsRepository));

        when(userRepository.findAllByType(User.Type.STUDENT))
                .thenReturn(List.of(user));

        when(internshipApplicationRepository.findAllByStudentUniqueIdAndSemester(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(internshipApplication));

        when(settingsRepository.findAll()).thenReturn(List.of(settings));

        // Act

        UserListReponse response = userService.studentsWithApplication();

        // Assert

        for (UserListReponse.User user1 : response.getUsers()) {

            assertEquals(user.getUniqueId(), user1.getUniqueId());
            assertEquals(user.getType().toString(), user1.getType());
            assertEquals(user.getEmail(), user1.getEmail());
            assertEquals(user.getFirstName(), user1.getFirstName());
            assertEquals(user.getLastName(), user1.getLastName());
            assertEquals(user.getCompany(), user1.getCompany());

        }
    }

    @Test
    public void employerUsers_validRequest() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.EMPLOYER);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");

        UserService userService = new UserService(userRepository, internshipApplicationRepository,internshipOfferRepository, new SettingsService(settingsRepository));

        when(userRepository.findAllByType(Mockito.any()))
                .thenReturn(List.of(user));

        // Act

        UserListReponse response = userService.employers();

        // Assert

        for (UserListReponse.User user1 : response.getUsers()) {

            assertEquals(user.getUniqueId(), user1.getUniqueId());
            assertEquals(user.getType().toString(), user1.getType());
            assertEquals(user.getEmail(), user1.getEmail());
            assertEquals(user.getFirstName(), user1.getFirstName());
            assertEquals(user.getLastName(), user1.getLastName());
            assertEquals(user.getCompany(), user1.getCompany());

        }
    }

    @Test
    public void employerWithOffer_validRequest() {
        // Arrange

        Settings settings = new Settings();
        settings.setCreationTimestamp(new Date().getTime());
        settings.setSemester("2020-03-16");

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.EMPLOYER);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setEmployer(user.getUniqueId());
        internshipOffer.setStatus(InternshipOffer.Status.REJECTED);
        internshipOffer.setCompany("Test Company");
        internshipOffer.setJobTitle("Test Job Title");
        internshipOffer.setStartDate(new Date());
        internshipOffer.setEndDate(new Date());
        internshipOffer.setLocation("test");
        internshipOffer.setSalary(20);
        internshipOffer.setHours(40);
        internshipOffer.setUsers(new ArrayList<>());

        UserService userService = new UserService(userRepository, internshipApplicationRepository,internshipOfferRepository, new SettingsService(settingsRepository));

        when(userRepository.findAllByType(Mockito.any()))
                .thenReturn(List.of(user));

        when(internshipOfferRepository.findAllByEmployerAndStatusAndSemester(Mockito.any(),Mockito.any(), Mockito.any()))
                .thenReturn(List.of(internshipOffer));

        when(settingsRepository.findAll()).thenReturn(List.of(settings));

        // Act

        UserListReponse response = userService.employersWithOffers();

        // Assert

        for (UserListReponse.User user1 : response.getUsers()) {

            assertEquals(user.getUniqueId(), user1.getUniqueId());
            assertEquals(user.getType().toString(), user1.getType());
            assertEquals(user.getEmail(), user1.getEmail());
            assertEquals(user.getFirstName(), user1.getFirstName());
            assertEquals(user.getLastName(), user1.getLastName());
            assertEquals(user.getCompany(), user1.getCompany());

        }
    }

    @Test
    public void employerWithoutOffer_validRequest() {
        // Arrange

        Settings settings = new Settings();
        settings.setCreationTimestamp(new Date().getTime());
        settings.setSemester("2020-03-16");

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.EMPLOYER);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setEmployer(user.getUniqueId());
        internshipOffer.setStatus(InternshipOffer.Status.REJECTED);
        internshipOffer.setCompany("Test Company");
        internshipOffer.setJobTitle("Test Job Title");
        internshipOffer.setStartDate(new Date());
        internshipOffer.setEndDate(new Date());
        internshipOffer.setLocation("test");
        internshipOffer.setSalary(20);
        internshipOffer.setHours(40);
        internshipOffer.setUsers(new ArrayList<>());

        UserService userService = new UserService(userRepository, internshipApplicationRepository,internshipOfferRepository, new SettingsService(settingsRepository));

        when(userRepository.findAllByType(Mockito.any()))
                .thenReturn(List.of(user));

        when(internshipOfferRepository.findAllByEmployerAndStatusAndSemester(Mockito.any(),Mockito.any(), Mockito.any()))
                .thenReturn(List.of(internshipOffer));

        when(settingsRepository.findAll()).thenReturn(List.of(settings));

        // Act

        UserListReponse response = userService.employersWithoutOffers();

        // Assert

        for (UserListReponse.User user1 : response.getUsers()) {

            assertEquals(user.getUniqueId(), user1.getUniqueId());
            assertEquals(user.getType().toString(), user1.getType());
            assertEquals(user.getEmail(), user1.getEmail());
            assertEquals(user.getFirstName(), user1.getFirstName());
            assertEquals(user.getLastName(), user1.getLastName());
            assertEquals(user.getCompany(), user1.getCompany());

        }
    }

    @Test
    public void findUserById_validRequest() {

        // Arrange
        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.EMPLOYER);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");

        UserListReponse.User userToFind = UserListReponse.map(user);

        UserService userService = new UserService(userRepository, internshipApplicationRepository,internshipOfferRepository, new SettingsService(settingsRepository));

        // Act
        when(userRepository.findById(any())).thenReturn(Optional.of(user));


        // Assert
        assertEquals(userToFind, userService.find(user.getUniqueId()));

    }


}

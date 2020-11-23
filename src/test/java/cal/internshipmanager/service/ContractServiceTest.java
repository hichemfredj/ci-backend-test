package cal.internshipmanager.service;

import cal.internshipmanager.model.*;
import cal.internshipmanager.repository.ContractRepository;
import cal.internshipmanager.repository.InternshipApplicationRepository;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.response.ContractListResponse;
import cal.internshipmanager.response.DownloadFileResponse;
import cal.internshipmanager.security.JwtAuthentication;
import cal.internshipmanager.security.JwtProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InternshipApplicationRepository internshipApplicationRepository;

    @Mock
    SettingsService settingsService;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    public void create_validRequest(){

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.ADMINISTRATOR);
        user.setEmail("admin@admin.com");
        user.setFirstName("Admin");
        user.setLastName("Admin");

        String token = jwtProvider.generate(user);
        DecodedJWT decodedToken = jwtProvider.verify(token);
        JwtAuthentication authentication = new JwtAuthentication(decodedToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User student = new User();
        student.setUniqueId(UUID.randomUUID());
        student.setType(User.Type.STUDENT);
        student.setEmail("student@student.com");
        student.setFirstName("student");
        student.setLastName("student");


        User employer = new User();
        employer.setUniqueId(UUID.randomUUID());
        employer.setType(User.Type.EMPLOYER);
        employer.setEmail("employer@employer.com");
        employer.setFirstName("employer");
        employer.setLastName("employer");



        InternshipOffer internshipOffer = new InternshipOffer();
        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setEmployer(employer.getUniqueId());



        InternshipApplication application = new InternshipApplication();

        application.setUniqueId(UUID.randomUUID());
        application.setSemester(settingsService.getSemester());
        application.setStudent(student);
        application.setOffer(internshipOffer);
        application.setStatus(InternshipApplication.Status.APPROVED);
        application.setDate(new Date());
        application.setInterviewDate(new Date());


        ContractService contractService = new ContractService(internshipApplicationRepository, userRepository, null, contractRepository, settingsService );

        when (userRepository.findById(user.getUniqueId())).thenReturn(Optional.of(user));
        when (internshipApplicationRepository.findById(application.getUniqueId())).thenReturn(Optional.of(application));

        when(contractRepository.save(any())).then(inv -> {

            Contract contract = inv.getArgument(0);

           assertNotNull(contract.getUniqueId());
           assertEquals( settingsService.getSemester(), contract.getSemester());
           assertEquals(application.getUniqueId(), contract.getApplication().getUniqueId());
           assertEquals(user, contract.getAdministrator());
           assertNotNull(contract.getCreationDate());
           assertNull(contract.getStudentSignature());
           assertNull(contract.getEmployerSignature());
           assertNull(contract.getAdministratorSignature());
           assertEquals(application.getStudent().getUniqueId(), contract.getCurrentUserUniqueId());

           return null;

        });

        contractService.create(application.getUniqueId());

    }

    @Test
    public void awaitSignature(){

        //ARRANGE

        User user = new User();
        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.ADMINISTRATOR);
        user.setEmail("admin@admin.com");
        user.setFirstName("Admin");
        user.setLastName("Admin");

        User employer = new User();
        employer.setUniqueId(UUID.randomUUID());
        employer.setType(User.Type.EMPLOYER);
        employer.setEmail("employer@employer.com");
        employer.setFirstName("employer");
        employer.setLastName("employer");

        User student = new User();
        student.setUniqueId(UUID.randomUUID());
        student.setType(User.Type.STUDENT);
        student.setEmail("student@student.com");
        student.setFirstName("student");
        student.setLastName("student");

        InternshipOffer internshipOffer = new InternshipOffer();
        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setEmployer(employer.getUniqueId());


        InternshipApplication application = new InternshipApplication();
        application.setUniqueId(UUID.randomUUID());
        application.setSemester(settingsService.getSemester());
        application.setStudent(student);
        application.setOffer(internshipOffer);
        application.setStatus(InternshipApplication.Status.APPROVED);
        application.setDate(new Date());
        application.setInterviewDate(new Date());


        Contract c = new Contract();
        c.setUniqueId(UUID.randomUUID());
        c.setSemester(settingsService.getSemester());
        c.setStatus(Contract.Status.STUDENT);
        c.setApplication(application);
        c.setAdministrator(user);
        c.setCreationDate(new Date());
        c.setStudentSignature(null);
        c.setEmployerSignature(null);
        c.setAdministratorSignature(null);
        c.setStudentSignedDate(new Date());
        c.setEmployerSignedDate(new Date());
        c.setAdministratorSignedDate(new Date());
        c.setCurrentUserUniqueId(application.getStudent().getUniqueId());

        ContractService contractService = new ContractService(internshipApplicationRepository, userRepository, null, contractRepository, settingsService );

        when (contractRepository.findAllBySemesterAndCurrentUserUniqueId(settingsService.getSemester(), c.getCurrentUserUniqueId())).thenReturn(List.of(c));


        // ACT

        ContractListResponse response = contractService.awaitingSignature(c.getCurrentUserUniqueId());

        ContractListResponse.Contract contract = response.getContracts().get(0);

        // ASSERT

        assertEquals(c.getUniqueId(), contract.getUniqueId());
        assertEquals(c.getSemester(), contract.getSemester());
        assertEquals(c.getApplication().getUniqueId(), contract.getApplication().getUniqueId());
        assertEquals(c.getAdministrator().getUniqueId(), contract.getAdministrator().getUniqueId());
        assertEquals(c.getCreationDate().getTime(), contract.getCreationDate());
        assertNull(contract.getStudentSignature());
        assertNull(contract.getEmployerSignature());
        assertNull(contract.getAdministratorSignature());
        assertNotNull(contract.getStudentSignedDate());
        assertNotNull(contract.getEmployerSignedDate());
        assertNotNull(contract.getAdministratorSignedDate());
        assertEquals(c.getCurrentUserUniqueId(), contract.getCurrentUserUniqueId());

    }

    @Test
    public void sign_student(){



        // Arrange


        Signature signature = new Signature();

        signature.setUniqueId(UUID.randomUUID());
        signature.setUploadDate(new Date());
        signature.setData(new byte[10]);

        User user = new User();
        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.STUDENT);
        user.setEmail("student@student.com");
        user.setFirstName("Student");
        user.setLastName("Student");
        user.setSignature(signature);

        String token = jwtProvider.generate(user);
        DecodedJWT decodedToken = jwtProvider.verify(token);
        JwtAuthentication authentication = new JwtAuthentication(decodedToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User employer = new User();
        employer.setUniqueId(UUID.randomUUID());
        employer.setType(User.Type.EMPLOYER);
        employer.setEmail("employer@employer.com");
        employer.setFirstName("employer");
        employer.setLastName("employer");

        InternshipOffer internshipOffer = new InternshipOffer();
        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setEmployer(employer.getUniqueId());


        InternshipApplication application = new InternshipApplication();
        application.setUniqueId(UUID.randomUUID());
        application.setSemester(settingsService.getSemester());
        application.setStudent(user);
        application.setOffer(internshipOffer);
        application.setStatus(InternshipApplication.Status.APPROVED);
        application.setDate(new Date());
        application.setInterviewDate(new Date());


        Contract contract = new Contract();

        contract.setUniqueId(UUID.randomUUID());
        contract.setCurrentUserUniqueId(user.getUniqueId());
        contract.setApplication(application);
        contract.setStatus(Contract.Status.STUDENT);

        ContractService contractService = new ContractService(internshipApplicationRepository, userRepository, null, contractRepository, settingsService );

        when(contractRepository.findById(contract.getUniqueId())).thenReturn(Optional.of(contract));
        when(userRepository.findById(user.getUniqueId())).thenReturn(Optional.of(user));



        // ACT



        when(contractRepository.save(any())).then(inv -> {

            Contract response = inv.getArgument(0);

            assertNotNull(response.getUniqueId());
            assertEquals(signature.getUniqueId(), response.getStudentSignature().getUniqueId());
            assertEquals(employer.getUniqueId(), response.getCurrentUserUniqueId());
            assertEquals(Contract.Status.EMPLOYER, response.getStatus());

            return null;

        });

        contractService.sign(contract.getUniqueId());

    }

    @Test
    public void sign_employer(){



        // Arrange


        Signature signature = new Signature();

        signature.setUniqueId(UUID.randomUUID());
        signature.setUploadDate(new Date());
        signature.setData(new byte[10]);

        User user = new User();
        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.EMPLOYER);
        user.setEmail("employer@employer.com");
        user.setFirstName("Employer");
        user.setLastName("Employer");
        user.setSignature(signature);

        String token = jwtProvider.generate(user);
        DecodedJWT decodedToken = jwtProvider.verify(token);
        JwtAuthentication authentication = new JwtAuthentication(decodedToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        User admin = new User();
        admin.setUniqueId(UUID.randomUUID());
        admin.setType(User.Type.ADMINISTRATOR);
        admin.setEmail("admin@admin.com");
        admin.setFirstName("Admin");
        admin.setLastName("Admin");

        InternshipOffer internshipOffer = new InternshipOffer();
        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setEmployer(user.getUniqueId());


        InternshipApplication application = new InternshipApplication();
        application.setUniqueId(UUID.randomUUID());
        application.setSemester(settingsService.getSemester());
        application.setOffer(internshipOffer);
        application.setStatus(InternshipApplication.Status.APPROVED);
        application.setDate(new Date());
        application.setInterviewDate(new Date());


        Contract contract = new Contract();

        contract.setUniqueId(UUID.randomUUID());
        contract.setCurrentUserUniqueId(user.getUniqueId());
        contract.setApplication(application);
        contract.setStatus(Contract.Status.EMPLOYER);
        contract.setAdministrator(admin);

        ContractService contractService = new ContractService(internshipApplicationRepository, userRepository, null, contractRepository, settingsService );

        when(contractRepository.findById(contract.getUniqueId())).thenReturn(Optional.of(contract));
        when(userRepository.findById(user.getUniqueId())).thenReturn(Optional.of(user));



        // ACT



        when(contractRepository.save(any())).then(inv -> {

            Contract response = inv.getArgument(0);

            assertNotNull(response.getUniqueId());
            assertEquals(signature.getUniqueId(), response.getEmployerSignature().getUniqueId());
            assertEquals(admin.getUniqueId(), response.getCurrentUserUniqueId());
            assertEquals(Contract.Status.ADMINISTRATOR, response.getStatus());

            return null;

        });

        contractService.sign(contract.getUniqueId());

    }


    @Test
    public void sign_admin(){


        // Arrange


        Signature signature = new Signature();

        signature.setUniqueId(UUID.randomUUID());
        signature.setUploadDate(new Date());
        signature.setData(new byte[10]);

        User user = new User();
        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.ADMINISTRATOR);
        user.setEmail("employer@employer.com");
        user.setFirstName("Employer");
        user.setLastName("Employer");
        user.setSignature(signature);

        String token = jwtProvider.generate(user);
        DecodedJWT decodedToken = jwtProvider.verify(token);
        JwtAuthentication authentication = new JwtAuthentication(decodedToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);



        Contract contract = new Contract();

        contract.setUniqueId(UUID.randomUUID());
        contract.setCurrentUserUniqueId(user.getUniqueId());
        contract.setStatus(Contract.Status.ADMINISTRATOR);
        contract.setAdministrator(user);

        ContractService contractService = new ContractService(internshipApplicationRepository, userRepository, null, contractRepository, settingsService );

        when(contractRepository.findById(contract.getUniqueId())).thenReturn(Optional.of(contract));
        when(userRepository.findById(user.getUniqueId())).thenReturn(Optional.of(user));



        // ACT



        when(contractRepository.save(any())).then(inv -> {

            Contract response = inv.getArgument(0);

            assertNotNull(response.getUniqueId());
            assertEquals(signature.getUniqueId(), response.getAdministratorSignature().getUniqueId());
            assertNull(response.getCurrentUserUniqueId());
            assertEquals(Contract.Status.COMPLETED, response.getStatus());

            return null;

        });

        contractService.sign(contract.getUniqueId());

    }

    @Test
    public void findUserById_validRequest() {

        // Arrange

        Signature signature = new Signature();

        signature.setUniqueId(UUID.randomUUID());
        signature.setUploadDate(new Date());

        Contract contract = new Contract();

        contract.setUniqueId(UUID.randomUUID());
        contract.setStatus(Contract.Status.COMPLETED);
        contract.setCreationDate(new Date());
        contract.setStudentSignature(signature);
        contract.setStudentSignedDate(new Date());
        contract.setEmployerSignedDate(new Date());
        contract.setAdministratorSignedDate(new Date());


        ContractService contractService = new ContractService(internshipApplicationRepository, userRepository, null, contractRepository, settingsService );

        when(contractRepository.findById(contract.getUniqueId())).thenReturn(Optional.of(contract));

        // Act

        ContractListResponse.Contract response = contractService.find(contract.getUniqueId());

        // Assert

        assertEquals(contract.getUniqueId(), response.getUniqueId());
        assertEquals(contract.getStatus().toString(), response.getStatus());
        assertNotNull(response.getStudentSignature());
        assertEquals(signature.getUniqueId(), response.getStudentSignature().getUniqueId());
        assertEquals(signature.getUploadDate().getTime(), response.getStudentSignature().getUploadDate());
    }

    @Test
    public void generate_validRequest(){

        // Arrange

        User student = new User();

        student.setUniqueId(UUID.randomUUID());
        student.setType(User.Type.STUDENT);
        student.setEmail("student@student.com");
        student.setFirstName("Student");
        student.setLastName("Student");

        User employer = new User();

        employer.setUniqueId(UUID.randomUUID());
        employer.setType(User.Type.EMPLOYER);
        employer.setEmail("employer@employer.com");
        employer.setFirstName("Employer");
        employer.setLastName("Employer");

        User admin = new User();

        admin.setUniqueId(UUID.randomUUID());
        admin.setType(User.Type.ADMINISTRATOR);
        admin.setEmail("admin@admin.com");
        admin.setFirstName("Admin");
        admin.setLastName("Admin");

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setEmployer(employer.getUniqueId());
        internshipOffer.setStartDate(new Date());
        internshipOffer.setEndDate(new Date());
        internshipOffer.setJobScope(new ArrayList<>());

        InternshipApplication application = new InternshipApplication();

        application.setUniqueId(UUID.randomUUID());
        application.setSemester(settingsService.getSemester());
        application.setStudent(student);
        application.setOffer(internshipOffer);
        application.setStatus(InternshipApplication.Status.APPROVED);
        application.setDate(new Date());
        application.setInterviewDate(new Date());

        Contract contract = new Contract();

        contract.setUniqueId(UUID.randomUUID());
        contract.setCurrentUserUniqueId(student.getUniqueId());
        contract.setApplication(application);
        contract.setStatus(Contract.Status.STUDENT);
        contract.setAdministrator(admin);

        ContractService contractService = new ContractService(internshipApplicationRepository, userRepository, null, contractRepository, settingsService );

        when(contractRepository.findById(contract.getUniqueId())).thenReturn(Optional.of(contract));
        when(userRepository.findById(employer.getUniqueId())).thenReturn(Optional.of(employer));

        // Act

        DownloadFileResponse response = contractService.generate(contract.getUniqueId());

        // Assert

        assertNotNull(response);
    }

}

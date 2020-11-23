package cal.internshipmanager.service;

import cal.internshipmanager.model.InternshipApplication;
import cal.internshipmanager.model.InternshipOffer;
import cal.internshipmanager.model.PortfolioDocument;
import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.InternshipApplicationRepository;
import cal.internshipmanager.repository.InternshipOfferRepository;
import cal.internshipmanager.repository.PortfolioDocumentRepository;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.request.InternshipApplicationCreationRequest;
import cal.internshipmanager.request.InternshipApplicationInterviewDateRequest;
import cal.internshipmanager.response.InternshipApplicationListResponse;
import cal.internshipmanager.response.PortfolioDocumentListResponse;
import cal.internshipmanager.security.JwtAuthentication;
import cal.internshipmanager.security.JwtProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class InternshipApplicationServiceTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Mock
    private SettingsService settingsService;

    @Mock
    private InternshipApplicationRepository internshipApplicationRepository;

    @Mock
    private PortfolioDocumentRepository portfolioDocumentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InternshipOfferRepository internshipOfferRepository;

    @Test
    public void portfolioDocuments_validRequest() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());

        List<PortfolioDocument> applicationDocuments = new ArrayList<>();
        PortfolioDocument portfolioDocument = new PortfolioDocument();

        portfolioDocument.setUniqueId(UUID.randomUUID());
        portfolioDocument.setUserUniqueId(user.getUniqueId());
        portfolioDocument.setFileName("Test1");
        portfolioDocument.setType("Test2");
        portfolioDocument.setUploadDate(new Date());
        portfolioDocument.setApproved(true);
        portfolioDocument.setData(new byte[]{1, 2, 3});

        applicationDocuments.add(portfolioDocument);

        InternshipApplication internshipApplication = new InternshipApplication();

        internshipApplication.setUniqueId(UUID.randomUUID());
        internshipApplication.setSemester(settingsService.getSemester());
        internshipApplication.setStudent(user);
        internshipApplication.setOffer(internshipOffer);
        internshipApplication.setStatus(InternshipApplication.Status.APPROVED);
        internshipApplication.setDocuments(applicationDocuments);
        internshipApplication.setDate(new Date());
        internshipApplication.setInterviewDate(new Date());

        InternshipApplicationService applicationService=new InternshipApplicationService(
                settingsService, internshipApplicationRepository, portfolioDocumentRepository, userRepository, internshipOfferRepository);

        when(internshipApplicationRepository.findById(internshipApplication.getUniqueId())).thenReturn(Optional.of(internshipApplication));

        //Act

        PortfolioDocumentListResponse response = applicationService.applicationDocuments(internshipApplication.getUniqueId());
        PortfolioDocumentListResponse.PortfolioDocument document = response.getPortfolioDocuments().get(0);

        //Assert

        assertEquals(portfolioDocument.getUniqueId(),document.getUniqueId());
        assertEquals(portfolioDocument.getType(),document.getType());
    }

    @Test
    public void internshipApplications_validRequest() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());

        InternshipApplication internshipApplication = new InternshipApplication();

        internshipApplication.setUniqueId(UUID.randomUUID());
        internshipApplication.setSemester(settingsService.getSemester());
        internshipApplication.setStudent(user);
        internshipApplication.setOffer(internshipOffer);
        internshipApplication.setStatus(InternshipApplication.Status.APPROVED);
        internshipApplication.setDate(new Date());
        internshipApplication.setInterviewDate(new Date());

        InternshipApplicationService internshipApplicationService = new InternshipApplicationService(
                settingsService, internshipApplicationRepository, portfolioDocumentRepository, userRepository, internshipOfferRepository);

        when(internshipApplicationRepository.findAllByStudentUniqueIdAndSemester(user.getUniqueId(), settingsService.getSemester()))
                .thenReturn(List.of(internshipApplication));

        // Act

        InternshipApplicationListResponse response = internshipApplicationService.internshipApplications(user.getUniqueId());

        InternshipApplicationListResponse.InternshipApplication application = response.getApplications().get(0);

        // Assert

        assertEquals(internshipApplication.getUniqueId(), application.getUniqueId());
        assertEquals(internshipApplication.getStudent().getUniqueId(), application.getStudentUniqueId());
        assertEquals(internshipApplication.getOffer().getUniqueId(), application.getOfferUniqueId());
        assertEquals(internshipApplication.getDate().getTime(), application.getDate());
        assertEquals(internshipApplication.getInterviewDate().getTime(), application.getInterviewDate());

    }

    @Test
    public void create_requireApproval() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.STUDENT);
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");

        InternshipOffer offer = new InternshipOffer();

        offer.setUniqueId(UUID.randomUUID());
        offer.setSemester(settingsService.getSemester());
        offer.setCompany("TestCompany");
        offer.setJobTitle("TestJobTitle");

        PortfolioDocument document = new PortfolioDocument();

        document.setUniqueId(UUID.randomUUID());

        String token = jwtProvider.generate(user);
        DecodedJWT decodedToken = jwtProvider.verify(token);
        JwtAuthentication authentication = new JwtAuthentication(decodedToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        InternshipApplicationService internshipApplicationService = new InternshipApplicationService(
                settingsService, internshipApplicationRepository, portfolioDocumentRepository, userRepository, internshipOfferRepository);

        InternshipApplicationCreationRequest request = new InternshipApplicationCreationRequest();

        request.setOfferUniqueId(offer.getUniqueId());
        request.setDocuments(List.of(document.getUniqueId()));

        when(settingsService.getRequireApproval()).thenReturn(true);

        when(userRepository.findById(user.getUniqueId())).thenReturn(Optional.of(user));

        when(internshipOfferRepository.findById(offer.getUniqueId())).thenReturn(Optional.of(offer));

        when(portfolioDocumentRepository.findById(document.getUniqueId())).thenReturn(Optional.of(document));

        // Act & Assert

        when(internshipApplicationRepository.save(any())).then(inv -> {

            InternshipApplication internshipApplication = inv.getArgument(0);

            assertEquals(settingsService.getSemester(), internshipApplication.getSemester());
            assertEquals(request.getOfferUniqueId(), internshipApplication.getOffer().getUniqueId());
            assertEquals(user.getUniqueId(), internshipApplication.getStudent().getUniqueId());
            assertEquals(document.getUniqueId(), internshipApplication.getDocuments().get(0).getUniqueId());
            assertEquals(InternshipApplication.Status.PENDING_APPROVAL, internshipApplication.getStatus());

            return null;
        });

        internshipApplicationService.create(request);
    }

    @Test
    public void create_noRequireApproval() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.STUDENT);
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");

        InternshipOffer offer = new InternshipOffer();

        offer.setUniqueId(UUID.randomUUID());
        offer.setSemester(settingsService.getSemester());
        offer.setCompany("TestCompany");
        offer.setJobTitle("TestJobTitle");

        PortfolioDocument document = new PortfolioDocument();

        document.setUniqueId(UUID.randomUUID());

        String token = jwtProvider.generate(user);
        DecodedJWT decodedToken = jwtProvider.verify(token);
        JwtAuthentication authentication = new JwtAuthentication(decodedToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        InternshipApplicationService internshipApplicationService = new InternshipApplicationService(
                settingsService, internshipApplicationRepository, portfolioDocumentRepository, userRepository, internshipOfferRepository);

        InternshipApplicationCreationRequest request = new InternshipApplicationCreationRequest();

        request.setOfferUniqueId(offer.getUniqueId());
        request.setDocuments(List.of(document.getUniqueId()));

        when(settingsService.getRequireApproval()).thenReturn(false);

        when(userRepository.findById(user.getUniqueId())).thenReturn(Optional.of(user));

        when(internshipOfferRepository.findById(offer.getUniqueId())).thenReturn(Optional.of(offer));

        when(portfolioDocumentRepository.findById(document.getUniqueId())).thenReturn(Optional.of(document));

        // Act & Assert

        when(internshipApplicationRepository.save(any())).then(inv -> {

            InternshipApplication internshipApplication = inv.getArgument(0);

            assertEquals(settingsService.getSemester(), internshipApplication.getSemester());
            assertEquals(request.getOfferUniqueId(), internshipApplication.getOffer().getUniqueId());
            assertEquals(user.getUniqueId(), internshipApplication.getStudent().getUniqueId());
            assertEquals(document.getUniqueId(), internshipApplication.getDocuments().get(0).getUniqueId());
            assertEquals(InternshipApplication.Status.APPROVED, internshipApplication.getStatus());

            return null;
        });

        internshipApplicationService.create(request);
    }

    @Test
    public void findByStatus_validRequest() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setType(User.Type.STUDENT);

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setSemester(settingsService.getSemester());
        internshipOffer.setCompany("TestCompany");
        internshipOffer.setJobTitle("TestJobTitle");


        InternshipApplication internshipApplication = new InternshipApplication();

        internshipApplication.setUniqueId(UUID.randomUUID());
        internshipApplication.setSemester(settingsService.getSemester());
        internshipApplication.setStudent(user);
        internshipApplication.setOffer(internshipOffer);
        internshipApplication.setDate(new Date());
        internshipApplication.setInterviewDate(new Date());
        internshipApplication.setStatus(InternshipApplication.Status.PENDING_APPROVAL);

        InternshipApplicationService internshipApplicationService = new InternshipApplicationService(
                settingsService, internshipApplicationRepository, portfolioDocumentRepository, userRepository, internshipOfferRepository);

        when(internshipApplicationRepository.findAllByStatusAndSemester(any(), any()))
                .thenReturn(List.of(internshipApplication));
        // Act

        InternshipApplicationListResponse response = internshipApplicationService.findByStatus(
                InternshipApplication.Status.PENDING_APPROVAL);

        // Assert

        assertEquals(1, response.getApplications().size());

        InternshipApplicationListResponse.InternshipApplication application = response.getApplications().get(0);

        assertEquals(internshipApplication.getUniqueId(), application.getUniqueId());
        assertEquals(internshipApplication.getStudent().getUniqueId(), application.getStudentUniqueId());
        assertEquals(user.getFirstName(), application.getStudentFirstName());
        assertEquals(user.getLastName(), application.getStudentLastName());
        assertEquals(internshipOffer.getCompany(), application.getCompany());
        assertEquals(internshipOffer.getJobTitle(), application.getJobTitle());
        assertEquals(internshipApplication.getDate().getTime(), application.getDate());
        assertEquals(internshipApplication.getInterviewDate().getTime(), application.getInterviewDate());
        assertEquals(InternshipApplication.Status.PENDING_APPROVAL.toString(), application.getStatus());
    }

    @Test
    public void findByOffer_validRequest() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setType(User.Type.STUDENT);

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setSemester(settingsService.getSemester());
        internshipOffer.setCompany("TestCompany");
        internshipOffer.setJobTitle("TestJobTitle");

        InternshipApplication internshipApplication = new InternshipApplication();

        internshipApplication.setUniqueId(UUID.randomUUID());
        internshipApplication.setSemester(settingsService.getSemester());
        internshipApplication.setOffer(internshipOffer);
        internshipApplication.setStudent(user);
        internshipApplication.setDate(new Date());
        internshipApplication.setInterviewDate(new Date());
        internshipApplication.setStatus(InternshipApplication.Status.APPROVED);

        InternshipApplicationListResponse response = new InternshipApplicationListResponse();

        InternshipApplicationService internshipApplicationService = new InternshipApplicationService(
                settingsService, internshipApplicationRepository, portfolioDocumentRepository, userRepository, internshipOfferRepository);

        when(internshipApplicationRepository.findAllByOfferUniqueIdAndStatusAndSemester(any(), any(), any()))
                .thenReturn(List.of(internshipApplication));

        response.setApplications(List.of(internshipApplication).stream()
                .map(InternshipApplicationListResponse::map)
                .collect(Collectors.toList()));

        when(internshipApplicationRepository.findById(internshipApplication.getUniqueId()))
                .thenReturn(Optional.of(internshipApplication));

        // Act

        InternshipApplicationListResponse responseToExpect = internshipApplicationService
                .findByOffer(internshipApplication.getOffer().getUniqueId());

        // Assert

        assertEquals(response, responseToExpect);
    }

    @Test
    public void approve_validRequest(){

        // Arrange

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setCompany("TestCompany");
        internshipOffer.setJobTitle("TestJobTitle");

        InternshipApplication internshipApplication = new InternshipApplication();

        internshipApplication.setUniqueId(UUID.randomUUID());
        internshipApplication.setOffer(internshipOffer);
        internshipApplication.setDate(new Date());
        internshipApplication.setStatus(InternshipApplication.Status.PENDING_APPROVAL);

        InternshipApplicationService internshipApplicationService = new InternshipApplicationService(
                settingsService, internshipApplicationRepository, portfolioDocumentRepository, userRepository, internshipOfferRepository);

        when(internshipApplicationRepository.findById(any())).thenReturn(Optional.of(internshipApplication));

        // Act & Assert

        when(internshipApplicationRepository.save(any())).then(inv -> {

            InternshipApplication application = inv.getArgument(0);

            assertEquals(InternshipApplication.Status.APPROVED,application.getStatus());

            return null;
        });

        internshipApplicationService.approve(internshipApplication.getUniqueId());

    }

    @Test
    public void reject_validRequest(){

        // Arrange

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setCompany("TestCompany");
        internshipOffer.setJobTitle("TestJobTitle");

        InternshipApplication internshipApplication = new InternshipApplication();

        internshipApplication.setUniqueId(UUID.randomUUID());
        internshipApplication.setOffer(internshipOffer);
        internshipApplication.setDate(new Date());
        internshipApplication.setStatus(InternshipApplication.Status.PENDING_APPROVAL);

        InternshipApplicationService internshipApplicationService = new InternshipApplicationService(
                settingsService, internshipApplicationRepository, portfolioDocumentRepository, userRepository, internshipOfferRepository);

        when(internshipApplicationRepository.findById(any())).thenReturn(Optional.of(internshipApplication));

        // Act & Assert

        when(internshipApplicationRepository.save(any())).then(inv -> {

            InternshipApplication application = inv.getArgument(0);

            assertEquals(InternshipApplication.Status.REJECTED,application.getStatus());

            return null;
        });

        internshipApplicationService.reject(internshipApplication.getUniqueId());

    }

    @Test
    public void select_validRequest(){

        // Arrange

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setCompany("TestCompany");
        internshipOffer.setJobTitle("TestJobTitle");

        InternshipApplication internshipApplication = new InternshipApplication();

        internshipApplication.setUniqueId(UUID.randomUUID());
        internshipApplication.setOffer(internshipOffer);
        internshipApplication.setDate(new Date());
        internshipApplication.setInterviewDate(new Date());
        internshipApplication.setStatus(InternshipApplication.Status.SELECTED);

        InternshipApplicationService internshipApplicationService = new InternshipApplicationService(
                settingsService, internshipApplicationRepository, portfolioDocumentRepository, userRepository, internshipOfferRepository);

        when(internshipApplicationRepository.findById(any())).thenReturn(Optional.of(internshipApplication));

        // Act & Assert

        when(internshipApplicationRepository.save(any())).then(inv -> {

            InternshipApplication application = inv.getArgument(0);

            assertEquals(InternshipApplication.Status.SELECTED, application.getStatus());

            return null;
        });

        internshipApplicationService.select(internshipApplication.getUniqueId());

    }

    @Test
    public void addInterview_validRequest(){

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setType(User.Type.STUDENT);

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setSemester(settingsService.getSemester());
        internshipOffer.setCompany("TestCompany");
        internshipOffer.setJobTitle("TestJobTitle");

        InternshipApplication internshipApplication = new InternshipApplication();

        internshipApplication.setUniqueId(UUID.randomUUID());
        internshipApplication.setSemester(settingsService.getSemester());
        internshipApplication.setStudent(user);
        internshipApplication.setOffer(internshipOffer);
        internshipApplication.setDate(new Date());
        internshipApplication.setInterviewDate(new Date());
        internshipApplication.setStatus(InternshipApplication.Status.PENDING_APPROVAL);

        InternshipApplicationInterviewDateRequest request = new InternshipApplicationInterviewDateRequest();

        request.setInterviewDate(new Date(0).getTime());

        InternshipApplicationService internshipApplicationService = new InternshipApplicationService(
                settingsService, internshipApplicationRepository, portfolioDocumentRepository, userRepository, internshipOfferRepository);

        when(internshipApplicationRepository.findById(internshipApplication.getUniqueId())).thenReturn(Optional.of(internshipApplication));

        // Act & Assert

        when(internshipApplicationRepository.save(any())).then(inv -> {

            InternshipApplication application = inv.getArgument(0);

            assertEquals(new Date(0),application.getInterviewDate());

            return null;
        });

        internshipApplicationService.addInterview(internshipApplication.getUniqueId(),request);
    }
}

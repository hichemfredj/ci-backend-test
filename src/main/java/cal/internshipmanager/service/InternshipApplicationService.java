package cal.internshipmanager.service;

import cal.internshipmanager.model.*;
import cal.internshipmanager.repository.InternshipApplicationRepository;
import cal.internshipmanager.repository.InternshipOfferRepository;
import cal.internshipmanager.repository.PortfolioDocumentRepository;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.request.InternshipApplicationCreationRequest;
import cal.internshipmanager.request.InternshipApplicationInterviewDateRequest;
import cal.internshipmanager.response.InternshipApplicationListResponse;
import cal.internshipmanager.response.PortfolioDocumentListResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InternshipApplicationService {

    //
    // Dependencies
    //

    private final SettingsService settingsService;

    private final InternshipApplicationRepository internshipApplicationRepository;

    private final PortfolioDocumentRepository portfolioDocumentRepository;

    private final UserRepository userRepository;

    private final InternshipOfferRepository internshipOfferRepository;

    //
    // Constructors
    //

    public InternshipApplicationService(SettingsService settingsService,
                                        InternshipApplicationRepository internshipApplicationRepository,
                                        PortfolioDocumentRepository portfolioDocumentRepository,
                                        UserRepository userRepository,
                                        InternshipOfferRepository internshipOfferRepository) {
        this.settingsService = settingsService;
        this.internshipApplicationRepository = internshipApplicationRepository;
        this.portfolioDocumentRepository = portfolioDocumentRepository;
        this.userRepository = userRepository;
        this.internshipOfferRepository = internshipOfferRepository;
    }

    //
    // Services
    //

    public void create(InternshipApplicationCreationRequest request) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UUID userUniqueId = UUID.fromString((String) authentication.getPrincipal());

        User student = userRepository.findById(userUniqueId).orElse(null);

        InternshipOffer offer = internshipOfferRepository.findById(request.getOfferUniqueId()).orElse(null);

        boolean requireApproval = settingsService.getRequireApproval();

        List<PortfolioDocument> documents = request.getDocuments().stream()
                .map(portfolioDocumentRepository::findById)
                .map(Optional::get).collect(Collectors.toList());

        InternshipApplication internshipApplication = new InternshipApplication();

        internshipApplication.setUniqueId(UUID.randomUUID());
        internshipApplication.setSemester(settingsService.getSemester());
        internshipApplication.setStudent(student);
        internshipApplication.setOffer(offer);
        //internshipApplication.setStudentUniqueId(student.getUniqueId());
        //internshipApplication.setOfferUniqueId(offer.getUniqueId());
        internshipApplication.setDate(new Date());
        internshipApplication.setInterviewDate(new Date(0));
        internshipApplication.setDocuments(documents);
        internshipApplication.setStatus(requireApproval
                ? InternshipApplication.Status.PENDING_APPROVAL
                : InternshipApplication.Status.APPROVED);

        internshipApplicationRepository.save(internshipApplication);
    }

    public InternshipApplicationListResponse internshipApplications(UUID userUniqueId) {

        List<InternshipApplication> applications = internshipApplicationRepository.findAllByStudentUniqueIdAndSemester(
                userUniqueId, settingsService.getSemester());

        InternshipApplicationListResponse response = new InternshipApplicationListResponse();

        response.setApplications(applications.stream()
                .map(InternshipApplicationListResponse::map)
                .collect(Collectors.toList()));

        return response;
    }

    public InternshipApplicationListResponse findByStatus(InternshipApplication.Status status) {

        List<InternshipApplication> applications = internshipApplicationRepository.findAllByStatusAndSemester(
                status, settingsService.getSemester());

        InternshipApplicationListResponse response = new InternshipApplicationListResponse();

        response.setApplications(applications.stream()
                .map(InternshipApplicationListResponse::map)
                .collect(Collectors.toList()));

        return response;
    }

    public void approve(UUID uniqueId) {

        InternshipApplication application = internshipApplicationRepository.findById(uniqueId).orElse(null);

        application.setStatus(InternshipApplication.Status.APPROVED);

        internshipApplicationRepository.save(application);
    }

    public void reject(UUID uniqueId) {

        InternshipApplication application = internshipApplicationRepository.findById(uniqueId).orElse(null);

        application.setStatus(InternshipApplication.Status.REJECTED);

        internshipApplicationRepository.save(application);
    }

    public void select(UUID uniqueId) {
        InternshipApplication application = internshipApplicationRepository.findById(uniqueId).orElse(null);

        application.setStatus(InternshipApplication.Status.SELECTED);

        internshipApplicationRepository.save(application);
    }

    public InternshipApplicationListResponse findByOffer(UUID uniqueId) {

        List<InternshipApplication> applications = internshipApplicationRepository.findAllByOfferUniqueIdAndStatusAndSemester(
                uniqueId, InternshipApplication.Status.APPROVED, settingsService.getSemester());

        InternshipApplicationListResponse response = new InternshipApplicationListResponse();

        response.setApplications(applications.stream()
                .map(InternshipApplicationListResponse::map)
                .collect(Collectors.toList()));

        return response;
    }

    public PortfolioDocumentListResponse applicationDocuments(UUID uniqueId) {

        InternshipApplication application = internshipApplicationRepository.findById(uniqueId).orElse(null);

        PortfolioDocumentListResponse response = new PortfolioDocumentListResponse();

        response.setPortfolioDocuments(application.getDocuments().stream()
                .map(PortfolioDocumentListResponse::map).collect(Collectors.toList()));

        return response;
    }

    public void addInterview(UUID uniqueId,InternshipApplicationInterviewDateRequest request) {
        InternshipApplication application = internshipApplicationRepository.findById(uniqueId).orElse(null);

        application.setInterviewDate(new Date(request.getInterviewDate()));

        internshipApplicationRepository.save(application);
    }
}

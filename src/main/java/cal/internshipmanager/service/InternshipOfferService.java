package cal.internshipmanager.service;

import cal.internshipmanager.model.InternshipOffer;
import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.InternshipOfferRepository;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.request.*;
import cal.internshipmanager.response.InternshipOfferListResponse;
import cal.internshipmanager.response.UserListReponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InternshipOfferService {

    //
    // Dependencies
    //

    private final SettingsService settingsService;

    private final InternshipOfferRepository internshipOfferRepository;

    private final UserRepository userRepository;

    //
    // Constructors
    //

    @Autowired
    public InternshipOfferService(SettingsService settingsService, InternshipOfferRepository internshipOfferRepository, UserRepository userRepository) {
        this.settingsService = settingsService;
        this.internshipOfferRepository = internshipOfferRepository;
        this.userRepository = userRepository;
    }

    //
    // Services
    //

    public void create(InternshipOfferCreationRequest request) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UUID userUniqueId = UUID.fromString((String) authentication.getPrincipal());

        InternshipOffer internshipOffer = new InternshipOffer();

        internshipOffer.setUniqueId(UUID.randomUUID());
        internshipOffer.setSemester(settingsService.getSemester());
        internshipOffer.setEmployer(userUniqueId);
        internshipOffer.setStatus(InternshipOffer.Status.PENDING_APPROVAL);
        internshipOffer.setCompany(request.getCompany());
        internshipOffer.setHours(request.getHours());
        internshipOffer.setJobScope(request.getJobScope());
        internshipOffer.setJobTitle(request.getJobTitle());
        internshipOffer.setSalary(request.getSalary());
        internshipOffer.setStartDate(new Date(request.getStartDate()));
        internshipOffer.setEndDate(new Date(request.getEndDate()));
        internshipOffer.setLocation(request.getLocation());
        internshipOffer.setUsers(new ArrayList<>());

        internshipOfferRepository.save(internshipOffer);
    }

    public void approve(InternshipOfferApproveRequest request) {

        InternshipOffer internshipOffer = internshipOfferRepository.findById(request.getUniqueId()).orElse(null);

        internshipOffer.setStatus(InternshipOffer.Status.APPROVED);

        internshipOfferRepository.save(internshipOffer);
    }

    public void reject(InternshipOfferRejectRequest request) {

        InternshipOffer internshipOffer = internshipOfferRepository.findById(request.getUniqueId()).orElse(null);

        internshipOffer.setStatus(InternshipOffer.Status.REJECTED);

        internshipOfferRepository.save(internshipOffer);
    }

    public InternshipOfferListResponse pendingApproval() {

        List<InternshipOffer> internshipOffers = internshipOfferRepository.findAllByStatusAndSemester(
                InternshipOffer.Status.PENDING_APPROVAL, settingsService.getSemester());

        InternshipOfferListResponse response = new InternshipOfferListResponse();

        response.setInternshipOffers(internshipOffers.stream().map(InternshipOfferListResponse::map)
                .collect(Collectors.toList()));

        return response;
    }

    public InternshipOfferListResponse approved() {

        List<InternshipOffer> internshipOffers = internshipOfferRepository.findAllByStatusAndSemester(
                InternshipOffer.Status.APPROVED, settingsService.getSemester());

        InternshipOfferListResponse response = new InternshipOfferListResponse();

        response.setInternshipOffers(internshipOffers.stream().map(InternshipOfferListResponse::map)
                .collect(Collectors.toList()));

        return response;
    }

    public InternshipOfferListResponse rejected() {

        List<InternshipOffer> internshipOffers = internshipOfferRepository.findAllByStatusAndSemester(
                InternshipOffer.Status.REJECTED, settingsService.getSemester());

        InternshipOfferListResponse response = new InternshipOfferListResponse();

        response.setInternshipOffers(internshipOffers.stream().map(InternshipOfferListResponse::map)
                .collect(Collectors.toList()));

        return response;
    }

    public InternshipOfferListResponse accessible(UUID userUniqueId) {

        List<InternshipOffer> internshipOffers = internshipOfferRepository.findAllByStatusAndSemester(
                InternshipOffer.Status.APPROVED, settingsService.getSemester());

        InternshipOfferListResponse response = new InternshipOfferListResponse();

        response.setInternshipOffers(internshipOffers.stream()
                .filter(x -> x.getUsers().stream().anyMatch(y -> y.getUniqueId().equals(userUniqueId)))
                .map(InternshipOfferListResponse::map).collect(Collectors.toList()));

        return response;
    }

    public UserListReponse users(UUID uniqueId) {

        InternshipOffer internshipOffer = internshipOfferRepository.findById(uniqueId).orElse(null);

        UserListReponse response = new UserListReponse();

        response.setUsers(internshipOffer.getUsers().stream().map(UserListReponse::map)
                .collect(Collectors.toList()));

        return response;
    }

    public void addUser(InternshipOfferAddUserRequest request) {

        InternshipOffer internshipOffer = internshipOfferRepository.findById(
                request.getOfferUniqueId()).orElse(null);

        User user = userRepository.findById(request.getUserUniqueId()).orElse(null);

        internshipOffer.getUsers().add(user);

        internshipOfferRepository.save(internshipOffer);
    }

    public void removeUser(InternshipOfferRemoveUserRequest request) {

        InternshipOffer internshipOffer = internshipOfferRepository.findById(
                request.getOfferUniqueId()).orElse(null);

        User user = userRepository.findById(request.getUserUniqueId()).orElse(null);
        internshipOffer.getUsers().remove(user);

        internshipOfferRepository.save(internshipOffer);
    }

    public InternshipOfferListResponse findAllByEmployer(UUID uniqueId){

        List<InternshipOffer> internshipOffers = internshipOfferRepository.findAllByEmployerAndStatusAndSemester(
                uniqueId, InternshipOffer.Status.APPROVED, settingsService.getSemester());

        InternshipOfferListResponse response = new InternshipOfferListResponse();

        response.setInternshipOffers(internshipOffers.stream().map(InternshipOfferListResponse::map)
                .collect(Collectors.toList()));

        return response;
    }

}

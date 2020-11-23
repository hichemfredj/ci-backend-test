package cal.internshipmanager.controller;

import cal.internshipmanager.request.*;
import cal.internshipmanager.response.InternshipOfferListResponse;
import cal.internshipmanager.response.UserListReponse;
import cal.internshipmanager.service.InternshipOfferService;
import cal.internshipmanager.validator.ExistingInternshipOffer;
import cal.internshipmanager.validator.ExistingUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/internship-offer")
public class InternshipOfferController {

    //
    // Dependencies
    //

    private final InternshipOfferService internshipOfferService;

    //
    // Constructors
    //

    @Autowired
    public InternshipOfferController(InternshipOfferService internshipOfferService) {
        this.internshipOfferService = internshipOfferService;
    }

    //
    // Post
    //

    @PostMapping("create")
    public void create(@Valid @RequestBody InternshipOfferCreationRequest request) {
        internshipOfferService.create(request);
    }

    @PostMapping("approve")
    public void approve(@Valid @RequestBody InternshipOfferApproveRequest request) {
        internshipOfferService.approve(request);
    }

    @PostMapping("reject")
    public void reject(@Valid @RequestBody InternshipOfferRejectRequest request) {
        internshipOfferService.reject(request);
    }

    @PostMapping("add-user")
    public void addUser(@Valid @RequestBody InternshipOfferAddUserRequest request) {
        internshipOfferService.addUser(request);
    }

    @PostMapping("remove-user")
    public void removeUser(@Valid @RequestBody InternshipOfferRemoveUserRequest request) {
        internshipOfferService.removeUser(request);
    }

    //
    // Get
    //

    @GetMapping("pending-approval")
    public InternshipOfferListResponse pendingApproval() {
        return internshipOfferService.pendingApproval();
    }

    @GetMapping("approved")
    public InternshipOfferListResponse approved() {
        return internshipOfferService.approved();
    }

    @GetMapping("rejected")
    public InternshipOfferListResponse rejected() {
        return internshipOfferService.rejected();
    }

    @GetMapping("accessible/{userUniqueId}")
    public InternshipOfferListResponse accessible(@Valid @ExistingUser @PathVariable UUID userUniqueId) {
        return internshipOfferService.accessible(userUniqueId);
    }

    @GetMapping("users/{uniqueId}")
    public UserListReponse users(@Valid @ExistingInternshipOffer @PathVariable UUID uniqueId) {
        return internshipOfferService.users(uniqueId);
    }

    @GetMapping("employer/{uniqueId}")
    public InternshipOfferListResponse findAllByEmployer(@Valid @ExistingUser @PathVariable UUID uniqueId){
        return internshipOfferService.findAllByEmployer(uniqueId);
    }

}

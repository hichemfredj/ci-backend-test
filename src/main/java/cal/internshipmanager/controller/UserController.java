package cal.internshipmanager.controller;

import cal.internshipmanager.response.UserListReponse;
import cal.internshipmanager.service.UserService;
import cal.internshipmanager.validator.ExistingUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    //
    // Dependencies
    //

    private final UserService userService;

    //
    // Constructors
    //

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //
    // Get
    //

    @GetMapping("students")
    public UserListReponse students() {
        return userService.students();
    }

    @GetMapping("students-with-application")
    public UserListReponse studentsWithApplication() {
        return userService.studentsWithApplication();
    }

    @GetMapping("students-without-application")
    public UserListReponse studentsWithoutApplication() {
        return userService.studentsWithoutApplication();
    }

    @GetMapping("employers")
    public UserListReponse employers() {
        return userService.employers();
    }

    @GetMapping("employers-with-offer")
    public UserListReponse employersWithOffer() {
        return userService.employersWithOffers();
    }

    @GetMapping("employers-without-offer")
    public UserListReponse employersWithoutOffer() {
        return userService.employersWithoutOffers();
    }

    @GetMapping("{userId}")
    public UserListReponse.User find(@Valid @ExistingUser @PathVariable UUID userId) {
        return userService.find(userId);
    }
}

package cal.internshipmanager.controller;

import cal.internshipmanager.request.AuthenticationRequest;
import cal.internshipmanager.response.AuthenticationResponse;
import cal.internshipmanager.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthenticationController {

    //
    // Dependencies
    //

    private final AuthenticationService authenticationService;

    //
    // Constructors
    //

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    //
    // Post 
    //

    @PostMapping("authenticate")
    public Object authenticate(@Valid @RequestBody AuthenticationRequest request) {

        AuthenticationResponse response = authenticationService.authenticate(request);

        return response.getToken() == null ? new ResponseEntity<>(HttpStatus.UNAUTHORIZED) : response;
    }

}
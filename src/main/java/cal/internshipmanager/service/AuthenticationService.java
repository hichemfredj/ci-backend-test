package cal.internshipmanager.service;

import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.request.AuthenticationRequest;
import cal.internshipmanager.response.AuthenticationResponse;
import cal.internshipmanager.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    //
    // Dependencies
    //

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    //
    // Constructors
    //

    @Autowired
    public AuthenticationService(
            UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    //
    // Services
    //

    /**
     * Attempts to authenticate the user in request.
     * <p>
     * If the authentication is valid
     * a successful authentication response containing a temporary jwt token will be sent.
     * If the authentication failed, a unsuccessful authentication response will be sent.
     *
     * @param request authentication request
     * @return authentication response
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        final AuthenticationResponse response = new AuthenticationResponse();

        final User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {

            String token = jwtProvider.generate(user);

            response.setUserUniqueId(user.getUniqueId());
            response.setUserType(user.getType().toString());
            response.setToken(token);
        }

        return response;
    }

}

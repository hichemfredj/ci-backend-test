package cal.internshipmanager.security;

import cal.internshipmanager.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Json web token provider
 * <p>
 * Generates encoded json web token's in string format. Can also decode tokens
 * using the same key used to encode them.
 * <p>
 * The secret key must never leave this class and all encoding/decoding should
 * be done with this component.
 */
@Component
public final class JwtProvider {

    //
    // Data
    //

    /**
     * Algorithm used for jwt signing
     */
    private final Algorithm algorithm;

    /**
     * Reusable verifier instance
     */
    private final JWTVerifier verifier;

    /**
     * Token validity duration in milliseconds
     */
    private final long duration;

    //
    // Constructors
    //

    @Autowired
    public JwtProvider(@Value("${security.jwt.duration}") long durationHours) {
        algorithm = Algorithm.HMAC256(SecureRandom.getSeed(16));
        verifier = JWT.require(algorithm).build();
        duration = TimeUnit.HOURS.toMillis(durationHours);
    }

    //
    // Services
    //

    /**
     * Returns new json web token for user.
     *
     * @param user user to generate token for
     * @return json web token
     */
    public String generate(final User user) {

        final long time = System.currentTimeMillis();

        return JWT.create()
                .withSubject(user.getUniqueId().toString())
                .withClaim("Type", user.getType().toString())
                .withIssuedAt(new Date(time))
                .withExpiresAt(new Date(time + duration))
                .sign(algorithm);
    }

    /**
     * Decodes and verifies the jwt token
     *
     * @param token encoded json web token
     * @return verified decoded jwt token
     * @throws JWTVerificationException if the verification failed
     */
    public DecodedJWT verify(String token) throws JWTVerificationException {

        if (token == null)
            throw new JWTVerificationException("Token cannot be null");

        token = token.replace("Bearer ", "");

        return verifier.verify(token);
    }

}

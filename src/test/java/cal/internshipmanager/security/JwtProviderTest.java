package cal.internshipmanager.security;

import cal.internshipmanager.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    public void generateToken_user() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.STUDENT);

        // Act

        String token = jwtProvider.generate(user);

        DecodedJWT decodedToken = JWT.decode(token);

        // Assert

        assertNotNull(token);
    }

    @Test
    public void verify_validToken() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.STUDENT);

        // Act

        String token = jwtProvider.generate(user);

        DecodedJWT decodedToken = jwtProvider.verify(token);

        // Assert

        assertEquals(user.getUniqueId().toString(), decodedToken.getSubject());
        assertEquals(user.getType().toString(), decodedToken.getClaim("Type").asString());
    }

    @Test
    public void verify_invalidToken() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.STUDENT);

        // Act & Assert

        final String token = jwtProvider.generate(user);

        final StringBuilder sb = new StringBuilder(token);

        sb.deleteCharAt(token.length() / 2); // Should not be able to modify token

        assertThrows(JWTVerificationException.class, () -> jwtProvider.verify(sb.toString()));
    }
}

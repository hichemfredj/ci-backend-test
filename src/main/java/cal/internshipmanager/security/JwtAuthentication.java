package cal.internshipmanager.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;

/**
 * Json web token authentication
 * <p>
 * Implements spring authentication, and is used in the security context. Useful
 * for retrieving jwt claims in rest controllers. Also supports the
 * <code>@PreAuthorize()</code> annotation by using the granted authorities
 * provided by this token.
 * <p>
 * To pre-authorize mappings using this authentication the method
 * <code>hasAuthority('[authority]')</code> must be used in the expression.
 *
 * @see GrantedAuthority
 */
public class JwtAuthentication implements Authentication {

    //
    // Data
    //

    /**
     * Decoded json web token
     */
    private DecodedJWT token;

    //
    // Constructors
    //

    public JwtAuthentication(DecodedJWT token) {
        this.token = token;
    }

    //
    // Services
    //

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        final Claim type = token.getClaim("Type");

        return type.isNull() ? Set.of()
                : Set.of(new SimpleGrantedAuthority(type.asString()));
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Could not set authenticated state");
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getPrincipal() {
        return token.getSubject();
    }

    @Override
    public String getName() {
        return token.getSubject();
    }

    @Override
    public Object getDetails() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}

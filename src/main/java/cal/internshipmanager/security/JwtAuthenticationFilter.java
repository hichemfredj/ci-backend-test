package cal.internshipmanager.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Json web token authentication filter
 * <p>
 * Filter used to retrieve json web tokens from http request headers. If there
 * is a token that can be decoded successfully, it will be parsed into a jwt
 * authentication and be set inside of the security context.
 *
 * @see JwtAuthentication
 * @see JwtProvider
 * @see OncePerRequestFilter
 * @see SecurityContextHolder
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //
    // Dependencies
    //

    @Autowired
    private JwtProvider jwtProvider;

    //
    // Services
    //

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final DecodedJWT token = jwtProvider.verify(request.getHeader("Authorization"));

            if (token != null)
                SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(token));

        } catch (JWTVerificationException e) {
            // JWT Verification failed, do nothing
        }

        filterChain.doFilter(request, response);
    }

}

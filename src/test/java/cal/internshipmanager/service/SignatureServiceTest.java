package cal.internshipmanager.service;

import cal.internshipmanager.model.Signature;
import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.response.DownloadFileResponse;
import cal.internshipmanager.response.SignatureResponse;
import cal.internshipmanager.security.JwtAuthentication;
import cal.internshipmanager.security.JwtProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SignatureServiceTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    public void find_validRequest(){

        // Arrange
        Signature signature = new Signature();

        signature.setData("signature".getBytes());
        signature.setUniqueId(UUID.randomUUID());
        signature.setUploadDate(new Date());

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.EMPLOYER);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");
        user.setSignature(signature);

        SignatureService signatureService = new SignatureService(userRepository);

        // Act

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        SignatureResponse signatureResponse = signatureService.find(user.getUniqueId());

        // Assert

        assertEquals(signatureResponse.getUniqueId(),signature.getUniqueId());
        assertEquals(signatureResponse.getUploadDate(),signature.getUploadDate());
    }

    @Test
    public void find_invalidRequest(){

        // Arrange
        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.EMPLOYER);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");

        SignatureService signatureService = new SignatureService(userRepository);

        // Act

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        SignatureResponse signatureResponse = signatureService.find(user.getUniqueId());

        // Assert

        assertNull(signatureResponse);
    }

    @Test
    public void upload_validRequest() {
        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.EMPLOYER);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");

        String token = jwtProvider.generate(user);
        DecodedJWT decodedToken = jwtProvider.verify(token);
        JwtAuthentication authentication = new JwtAuthentication(decodedToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MockMultipartFile file = new MockMultipartFile("file", "img.png", "multipart/form-data", "salut".getBytes());

        SignatureService signatureService = new SignatureService(userRepository);

        // Act & Assert

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        when(userRepository.save(Mockito.any())).then(inv -> {
            User user1 = (User) inv.getArgument(0);
            Signature signature = user1.getSignature();

            assertEquals(user.getUniqueId(), user1.getUniqueId());
            assertEquals(user.getCompany(), user1.getCompany());
            assertEquals(user.getFirstName(), user1.getFirstName());
            assertEquals(user.getLastName(), user1.getLastName());
            assertEquals(user.getEmail(), user1.getEmail());
            assertEquals(signature.getData(), file.getBytes());
            assertNotNull(signature.getUploadDate());
            assertNotNull(signature.getUniqueId());

            return null;
        });

        // Assert

        signatureService.upload(file);
    }

    @Test
    public void download_validRequest() {

        // Arrange

        final String signatureName = "signature";
        final String signatureType = "image/png";


        Signature signature = new Signature();

        signature.setData("signature".getBytes());
        signature.setUniqueId(UUID.randomUUID());
        signature.setUploadDate(new Date());

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.EMPLOYER);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");
        user.setSignature(signature);

        final SignatureService signatureService = new SignatureService(userRepository);

        // Act

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        DownloadFileResponse response = signatureService.download(user.getUniqueId());

        // Assert

        assertEquals(response.getName(), signatureName);
        assertEquals(response.getType(), signatureType);
        assertEquals(response.getLength(), signature.getData().length);
        assertEquals(response.getResource(), new ByteArrayResource(signature.getData()));
    }

    @Test
    public void delete_validRequest() {

        // Arrange

        Signature signature = new Signature();

        signature.setData("signature".getBytes());
        signature.setUniqueId(UUID.randomUUID());
        signature.setUploadDate(new Date());

        User user = new User();

        user.setUniqueId(UUID.randomUUID());
        user.setType(User.Type.EMPLOYER);
        user.setEmail("toto@gmail.com");
        user.setFirstName("Toto");
        user.setLastName("Tata");
        user.setCompany("Test");
        user.setSignature(signature);

        SignatureService signatureService = new SignatureService(userRepository);

        // Act & Assert

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        when(userRepository.save(Mockito.any())).then(inv -> {
            User user1 = (User) inv.getArgument(0);

            assertNull(user1.getSignature());

            return null;
        });

        // Assert

        signatureService.delete(user.getUniqueId());
    }

}

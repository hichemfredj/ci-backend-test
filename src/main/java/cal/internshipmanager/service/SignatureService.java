package cal.internshipmanager.service;

import cal.internshipmanager.model.Signature;
import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.response.DownloadFileResponse;
import cal.internshipmanager.response.SignatureResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class SignatureService {

    private final UserRepository userRepository;


    @Autowired
    public SignatureService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SignatureResponse find(UUID userUniqueId) {
        User user = userRepository.findById(userUniqueId).get();
        Signature signature = user.getSignature();

        if (signature != null) {
            SignatureResponse response = new SignatureResponse();

            response.setUniqueId(signature.getUniqueId());
            response.setUploadDate(signature.getUploadDate());

            return response;
        }

        return null;
    }

    @SneakyThrows
    public void upload(MultipartFile file) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UUID userUniqueId = UUID.fromString((String) authentication.getPrincipal());

        Signature signature = new Signature();

        signature.setUniqueId(UUID.randomUUID());
        signature.setUploadDate(new Date());
        signature.setData(file.getBytes());

        User user = userRepository.findById(userUniqueId).get();

        user.setSignature(signature);

        userRepository.save(user);
    }

    public DownloadFileResponse download(UUID userUniqueId) {

        User user = userRepository.findById(userUniqueId).get();
        Signature signature = user.getSignature();

        DownloadFileResponse response = new DownloadFileResponse();

        response.setName("signature");
        response.setType("image/png");
        response.setResource(new ByteArrayResource(signature.getData()));
        response.setLength(signature.getData().length);

        return response;
    }

    public void delete(UUID userUniqueId) {
        User user = userRepository.findById(userUniqueId).get();

        user.setSignature(null);

        userRepository.save(user);
    }
}

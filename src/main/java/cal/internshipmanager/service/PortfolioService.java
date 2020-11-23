package cal.internshipmanager.service;

import cal.internshipmanager.model.PortfolioDocument;
import cal.internshipmanager.repository.PortfolioDocumentRepository;
import cal.internshipmanager.request.PortfolioDocumentDeleteRequest;
import cal.internshipmanager.response.DownloadFileResponse;
import cal.internshipmanager.response.PortfolioDocumentListResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    //
    // Dependencies
    //

    private final PortfolioDocumentRepository portfolioDocumentRepository;

    //
    // Constructors
    //

    @Autowired
    public PortfolioService(PortfolioDocumentRepository portfolioDocumentRepository) {
        this.portfolioDocumentRepository = portfolioDocumentRepository;
    }

    //
    // Services
    //

    @SneakyThrows
    public void upload(String type, MultipartFile file) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UUID userUniqueId = UUID.fromString((String) authentication.getPrincipal());

        PortfolioDocument portfolioDocument = new PortfolioDocument();

        portfolioDocument.setUniqueId(UUID.randomUUID());
        portfolioDocument.setUserUniqueId(userUniqueId);
        portfolioDocument.setFileName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
        portfolioDocument.setFileType(file.getContentType());
        portfolioDocument.setType(type);
        portfolioDocument.setUploadDate(new Date());
        portfolioDocument.setApproved(false);
        portfolioDocument.setData(file.getBytes());

        portfolioDocumentRepository.save(portfolioDocument);
    }

    public DownloadFileResponse download(UUID uniqueId) {

        PortfolioDocument portfolioDocument = portfolioDocumentRepository.findById(uniqueId).orElse(null);

        DownloadFileResponse response = new DownloadFileResponse();

        response.setName(portfolioDocument.getFileName());
        response.setType(portfolioDocument.getFileType());
        response.setResource(new ByteArrayResource(portfolioDocument.getData()));
        response.setLength(portfolioDocument.getData().length);

        return response;
    }

    public PortfolioDocumentListResponse portfolioDocuments(UUID userUniqueId) {

        List<PortfolioDocument> portfolioDocuments = portfolioDocumentRepository.findAllByUserUniqueId(userUniqueId);

        PortfolioDocumentListResponse response = new PortfolioDocumentListResponse();

        response.setPortfolioDocuments(portfolioDocuments.stream()
                .map(PortfolioDocumentListResponse::map).collect(Collectors.toList()));

        return response;
    }

    public PortfolioDocumentListResponse approved(UUID userUniqueId){

        List<PortfolioDocument> portfolioDocuments = portfolioDocumentRepository
                .findAllByUserUniqueIdAndApproved(userUniqueId, true);

        PortfolioDocumentListResponse response = new PortfolioDocumentListResponse();

        response.setPortfolioDocuments(portfolioDocuments.stream()
                .map(PortfolioDocumentListResponse::map).collect(Collectors.toList()));

        return response;
    }

    public void approve(UUID uniqueId){

        PortfolioDocument portfolioDocument = portfolioDocumentRepository.findById(uniqueId).orElse(null);

        portfolioDocument.setApproved(true);

        portfolioDocumentRepository.save(portfolioDocument);
    }

    public void delete(PortfolioDocumentDeleteRequest request) {
        portfolioDocumentRepository.deleteById(request.getUniqueId());
    }

}

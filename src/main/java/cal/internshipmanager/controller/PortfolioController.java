package cal.internshipmanager.controller;

import cal.internshipmanager.request.PortfolioDocumentDeleteRequest;
import cal.internshipmanager.response.DownloadFileResponse;
import cal.internshipmanager.response.PortfolioDocumentListResponse;
import cal.internshipmanager.service.PortfolioService;
import cal.internshipmanager.validator.ExistingPortfolioDocument;
import cal.internshipmanager.validator.ExistingUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    //
    // Dependencies
    //

    private final PortfolioService portfolioService;

    //
    // Constructors
    //

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    //
    // Post
    //

    @PostMapping("upload")
    public void upload(@Valid @NotBlank @RequestParam("type") String type,
                       @Valid @NotNull @RequestParam("file") MultipartFile file) {
        portfolioService.upload(type, file);
    }

    @PostMapping("delete")
    public void delete(@Valid @RequestBody PortfolioDocumentDeleteRequest request) {
        portfolioService.delete(request);
    }

    //
    // Put
    //

    @PutMapping("approve/{uniqueId}")
    public void approve(@Valid @ExistingPortfolioDocument @PathVariable UUID uniqueId) {
        portfolioService.approve(uniqueId);
    }

    //
    // Get
    //

    @GetMapping("{uniqueId}")
    public ResponseEntity<Resource> download(@Valid @ExistingPortfolioDocument @PathVariable UUID uniqueId) {
        return DownloadFileResponse.responseEntity(portfolioService.download(uniqueId));
    }

    @GetMapping("portfolio-documents/{userUniqueId}")
    public PortfolioDocumentListResponse portfolioDocuments(@Valid @ExistingUser @PathVariable UUID userUniqueId) {
        return portfolioService.portfolioDocuments(userUniqueId);
    }

    @GetMapping("approved/{userUniqueId}")
    public PortfolioDocumentListResponse approved(@Valid @ExistingUser @PathVariable UUID userUniqueId) {
        return portfolioService.approved(userUniqueId);
    }

}

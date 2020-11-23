package cal.internshipmanager.response;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DownloadFileResponseTests {

    @Test
    public void responseEntity() {

        // Arrange

        DownloadFileResponse response = new DownloadFileResponse();

        response.setLength(10);
        response.setType("application/json");
        response.setName("test");
        response.setResource(new ByteArrayResource(new byte[10]));

        // Act

        ResponseEntity<Resource> responseEntity = DownloadFileResponse.responseEntity(response);

        // Assert

        assertEquals(response.getLength(), responseEntity.getHeaders().getContentLength());
        assertEquals(MediaType.parseMediaType(response.getType()), responseEntity.getHeaders().getContentType());
        assertEquals(response.getName(), responseEntity.getHeaders().getContentDisposition().getFilename());
        assertEquals(response.getResource(), responseEntity.getBody());

    }
}

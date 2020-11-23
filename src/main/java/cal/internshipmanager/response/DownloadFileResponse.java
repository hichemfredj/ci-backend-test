package cal.internshipmanager.response;

import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
public class DownloadFileResponse implements Serializable {

    private String name;

    private String type;

    private Resource resource;

    private long length;

    //
    // Utils
    //

    public static ResponseEntity<Resource> responseEntity(final DownloadFileResponse response){
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(response.getType()))
                .contentLength(response.getLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.getName() + "\"")
                .body(response.getResource());
    }
}

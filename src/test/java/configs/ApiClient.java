package configs;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ApiClient extends AbstractTestApi {

    public ApiClient(TestRestTemplate restTemplate, int port) {
        super(restTemplate, port);
    }

    public ResponseEntity<String> getUploadForm() {
        ResponseEntity<String> response = getRequest("");

        assertEquals(200, response.getStatusCodeValue());
        return response;
    }

    public ResponseEntity<String> postUploadForm(List<URL> params) {
        ResponseEntity<String> response = postRequest("", null, params);
        // verify is page redirected
        assertEquals(HttpStatus.FOUND.value(), response.getStatusCodeValue());
        return response;
    }

    public ResponseEntity<Resource> getServeFile(String fileName) {
        ResponseEntity<Resource> response = getRequestResource("files/" + fileName + ".jpg");

        assertEquals(200, response.getStatusCodeValue());
        return response;
    }
}

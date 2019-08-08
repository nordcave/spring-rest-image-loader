import configs.BaseTest;
import configs.ResponseConverter;
import hello.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class FileUploadTest extends BaseTest {
    private String testFileLink = "http://www.ufstarfleet.org/wiki/images/b/b6/Blue-Planet-Earth.jpg";
    private ResponseConverter responseConverter = new ResponseConverter();

    @Test
    public void handleFileUploadTest() throws MalformedURLException {
        // verify that upload form is empty
        ResponseEntity<String> emptyUploadForm = apiClient.getUploadForm();
        Assert.assertTrue(emptyUploadForm.getBody().startsWith(ResponseConverter.HTML_ATTRIBUTE));
        Assert.assertFalse(emptyUploadForm.getBody().contains(ResponseConverter.UPLOADED_FILE_ATTRIBUTE));

        // upload file (use another random link if this one is not valid anymore)
        apiClient.postUploadForm(Arrays.asList(new URL(testFileLink)));

        // get upload form with uploaded file
        ResponseEntity<String> uploadForm = apiClient.getUploadForm();
        Assert.assertTrue(uploadForm.getBody().contains(ResponseConverter.UPLOADED_FILE_ATTRIBUTE));
        String fileName = responseConverter.getFileName(uploadForm.getBody());

        // download uploaded file
        ResponseEntity<Resource> servedFile = apiClient.getServeFile(fileName);
        assertTrue(servedFile.getBody().exists());
    }
}

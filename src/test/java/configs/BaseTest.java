package configs;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

public class BaseTest {
    @LocalServerPort
    protected int appPort;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected ApiClient apiClient;

    @Before
    public void beforeTest() {
        apiClient = new ApiClient(restTemplate, appPort);
    }
}

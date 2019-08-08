package configs;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.List;

public class AbstractTestApi {

    private int port;
    private TestRestTemplate restTemplate;

    public AbstractTestApi(TestRestTemplate restTemplate, int port) {
        this.port = port;
        this.restTemplate = restTemplate;
    }

    private String createURLWithPort(String uri) {
        String url = "http://localhost:" + port;
        return url + '/' + uri;
    }

    /**
     * Send GET request
     */
    public ResponseEntity<String> getRequest(String uri) {
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        return restTemplate.exchange(createURLWithPort(uri), HttpMethod.GET, entity, String.class);
    }

    /**
     * Send GET request for resource
     */
    public ResponseEntity<Resource> getRequestResource(String uri) {
        HttpEntity<Resource> entity = new HttpEntity<>(new HttpHeaders());
        return restTemplate.exchange(createURLWithPort(uri), HttpMethod.GET, entity, Resource.class);
    }

    /**
     * Send POST request
     */
    protected ResponseEntity<String> postRequest(String uri, String payload, List<URL> params) {
        HttpEntity entity;
        if (StringUtils.isBlank(payload)) {
            entity = new HttpEntity<>(new HttpHeaders());
        } else {
            entity = new HttpEntity<>(payload, new HttpHeaders());
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort(uri));
        params.stream().forEach(param -> builder.queryParam("file", param));

        return restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
    }


}

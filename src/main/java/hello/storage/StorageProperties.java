package hello.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;

/**
 *  Location management for uploaded images
 */
@Configuration
@ConfigurationProperties("storage")
public class StorageProperties {

    private String location;

    /**
     * Creates (child) temporary directory under main JVM-temp folder;
     * Clears after application termination
     */
    public StorageProperties() {
        {
            try {
                location = Files.createTempDirectory("images").toAbsolutePath().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}

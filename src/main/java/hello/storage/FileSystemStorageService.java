package hello.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

/**
 * Class that provides main functionality for storing images;
 * Implementation of StorageService interface
 */
@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    // initialization constructor
    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    /**
     * Actually stores images from a given list of URLs;
     * Method "recreates" images in .jpg format and generates new filename with randomUUID()
     * @param files - list of URLs from upload form
     */
    @Override
    public void storeFromUrl(List<URL> files) {
        try {
            if (files.equals(null)) {
                throw new StorageException("Failed to store empty file.");
            }

            BufferedImage image;

            for (int i=0; i<files.size(); i++)
            {
                image = ImageIO.read(files.get(i));
                String uniqueID = UUID.randomUUID().toString();
                ImageIO.write(image, "jpg", this.rootLocation.resolve(uniqueID + ".jpg").toFile());
            }

        } catch (IOException e) {
            throw new StorageException("Failed to store file ", e);
        }
    }

    /**
     * Method used to list uploaded files on the page
     * @return files
     */
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    /**
     * load() and loadAsResource() - methods that allows user
     * to download file that was uploaded earlier
     */
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    /**
     * possible future functionality - deleting files
     * (not required in current version)
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}

package hello.storage;

import org.springframework.core.io.Resource;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Interface - provides main functionality
 * more details are available in implementation
 */
public interface StorageService {

    void storeFromUrl(List<URL> files);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
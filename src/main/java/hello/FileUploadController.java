package hello;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.storage.StorageFileNotFoundException;
import hello.storage.StorageService;

/**
 * main application controller
 */
@Controller
public class FileUploadController {

    private final StorageService storageService;

    // initialization constructor
    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * method responsible for home-page construction
     * @param model - Spring Model
     * @return upload form
     * @throws IOException
     */
    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    /**
     * is liable for stored files listing
     * @param filename
     * @return response entity with files listing
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * transfers "file"'s from input form into the List of URLs to handle with Java
     * @param url - image upload URL
     * @param redirectAttributes - additional payload (like text message)
     * @return result page redirection
     */
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") List<URL> url,
                                   RedirectAttributes redirectAttributes) {
        storageService.storeFromUrl(url);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded the file! ");

        return "redirect:/";
    }

    /**
     * handles the case when URL is corrupted
     * @param exc thrown exception
     * @return result page
     */
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}

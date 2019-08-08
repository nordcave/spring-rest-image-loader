package configs;

public class ResponseConverter {

    public static final String HTML_ATTRIBUTE = "<html>";
    public static final String UPLOADED_FILE_ATTRIBUTE = "/files/";
    private static final String JPG_EXTENSION = ".jpg";


    public String getFileName(String response) {
        return response.split(UPLOADED_FILE_ATTRIBUTE)[1].split(JPG_EXTENSION)[0];
    }
}

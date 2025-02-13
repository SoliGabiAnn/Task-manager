package kod_aplikacji;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class FileHandler {
    private String filename;
    private File file;
    public FileHandler() throws IOException {//if we then want to have a lot o users
        this.file = new File("user.json");
    }

    public void writeToJsonFile( User user) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writerWithDefaultPrettyPrinter().writeValue(this.file, user);
    }

    // Generic method to read an object from a JSON file
    public User readFromJsonFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(this.file, User.class);
    }

    public File getFile() {
        return this.file;
    }
}

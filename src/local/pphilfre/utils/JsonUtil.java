package local.pphilfre.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A utility class for handling JSON serialization and deserialization.
 */
public class JsonUtil {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        // Configure ObjectMapper for pretty printing
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    /**
     * Loads an object from a JSON file.
     *
     * @param <T> The type of object to load
     * @param filePath The path to the JSON file
     * @param clazz The class of the object to load
     * @return The loaded object
     * @throws IOException If an error occurs during loading
     */
    public static <T> T loadObjectFromJsonFile(String filePath, Class<T> clazz) throws IOException {
        return objectMapper.readValue(new File(filePath), clazz);
    }
    
    /**
     * Loads a game definition from a JSON file, trying both direct path and classpath resource.
     *
     * @param <T> The type of object to load
     * @param filePath The path to the JSON file
     * @param clazz The class of the object to load
     * @return The loaded object
     * @throws IOException If an error occurs during loading
     */
    public static <T> T loadGameDefinition(String filePath, Class<T> clazz) throws IOException {
        // First try direct file path
        File file = new File(filePath);
        if (file.exists()) {
            return objectMapper.readValue(file, clazz);
        }
        
        // Then try classpath resource
        InputStream is = JsonUtil.class.getClassLoader().getResourceAsStream(filePath);
        if (is != null) {
            return objectMapper.readValue(is, clazz);
        }
        
        // Try relative path
        file = new File("." + File.separator + filePath);
        if (file.exists()) {
            return objectMapper.readValue(file, clazz);
        }
        
        throw new IOException("Could not find JSON file at path: " + filePath);
    }
    
    /**
     * Saves an object to a JSON file.
     *
     * @param object The object to save
     * @param filePath The path to the JSON file
     * @throws IOException If an error occurs during saving
     */
    public static void saveObjectToJsonFile(Object object, String filePath) throws IOException {
        // Ensure parent directories exist
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null) {
            Files.createDirectories(parentDir.toPath());
        }
        
        objectMapper.writeValue(file, object);
    }
}

package utils;


import io.restassured.path.json.JsonPath;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class ResourceUtils {

    public String getStringFromResource(String classpathPath) {
        try (InputStream inputStream = getClass().getResourceAsStream(classpathPath)) {
            if (inputStream == null) {
                throw new RuntimeException("File not found in classpath: " + classpathPath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + classpathPath, e);
        }
    }

    public String getFieldFromJsonString(String json, String jsonPath) {
        return JsonPath.from(json).getString(jsonPath);
    }
}

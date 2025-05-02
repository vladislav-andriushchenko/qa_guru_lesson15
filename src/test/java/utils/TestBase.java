package utils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestBase {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    public String generateStringFromResource(String classpathPath) {
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

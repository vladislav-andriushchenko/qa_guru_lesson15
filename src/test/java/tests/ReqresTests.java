package tests;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.TestBase;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ReqresTests extends TestBase {

    private static final String API_KEY = "reqres-free-v1";

    @Test
    void successfulRegisterTest() {
        String body = generateStringFromResource("/reqresData/bodyForRegister.json");
        String uri = "/register";

        given()
                .header("x-api-key", API_KEY)
                .body(body)
                .contentType(JSON)
                .log().all()

                .when()
                .post(uri)

                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(4))
                .body("token", notNullValue());
    }

    @ParameterizedTest()
    @ValueSource(strings = {"email", "password"})
    void unsuccessfulRegisterTest(String field) {
        String body = generateStringFromResource("/reqresData/bodyForRegister.json")
                .replace(field, "/" + field);

        String uri = "/register";

        given()
                .header("x-api-key", API_KEY)
                .body(body)
                .contentType(JSON)
                .log().all()

                .when()
                .post(uri)

                .then()
                .log().all()
                .statusCode(400)
                .body("error", containsString("Missing " + field));
    }

    @Test
    void successfulUpdateTest() {
        String body = generateStringFromResource("/reqresData/bodyForUpdate.json");

        String name = getFieldFromJsonString(body, "name");
        String job = getFieldFromJsonString(body, "job");

        String uri = "/users/2";

        given()
                .header("x-api-key", API_KEY)
                .body(body)
                .contentType(JSON)
                .log().all()

                .when()
                .put(uri)

                .then()
                .log().all()
                .statusCode(200)
                .body("updatedAt", notNullValue())
                .body("name", equalTo(name))
                .body("job", equalTo(job));
    }

    @Test
    void getUserTest() {
        String uri = "/users/2";

        given()
                .header("x-api-key", API_KEY)
                .log().all()

                .when()
                .get(uri)

                .then()
                .log().all()
                .statusCode(200)
                .body("data", hasKey("id"))
                .body("data", hasKey("email"))
                .body("data", hasKey("first_name"))
                .body("data", hasKey("last_name"))
                .body("data", hasKey("avatar"))
                .body("support", hasKey("url"))
                .body("support", hasKey("text"));
    }

    @Test
    void getNullUserTest() {
        String uri = "/users/23";

        given()
                .header("x-api-key", API_KEY)
                .log().all()

                .when()
                .get(uri)

                .then()
                .log().all()
                .statusCode(404)
                .body("isEmpty()", Matchers.is(true));
    }

    @Test
    void getUsersTest() {
        String uri = "/users";

        given()
                .header("x-api-key", API_KEY)
                .queryParam("page", "2")
                .log().all()

                .when()
                .get(uri)

                .then()
                .log().all()
                .statusCode(200)
                .body("page", is(2))
                .body("data", hasSize(6));
    }

    @Test
    void getEmptyUsersTest() {
        String uri = "/users";

        given()
                .header("x-api-key", API_KEY)
                .queryParam("page", "3")
                .log().all()

                .when()
                .get(uri)

                .then()
                .log().all()
                .statusCode(200)
                .body("page", is(3))
                .body("data", hasSize(0));
    }

    @Test
    void deleteUserTest() {
        String uri = "/users/2";

        given()
                .header("x-api-key", API_KEY)
                .log().all()

                .when()
                .delete(uri)

                .then()
                .log().all()
                .statusCode(204);
    }
}

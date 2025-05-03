package tests;

import io.restassured.response.Response;
import models.SingleUserResponse;
import models.UserListResponse;
import models.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import steps.ReqresSteps;
import utils.ResourceUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class ReqresTests {

    ReqresSteps reqresSteps = new ReqresSteps();
    ResourceUtils utils = new ResourceUtils();

    @Test
    @DisplayName("")
    void successfulRegisterTest() {
        String body = utils.getStringFromResource("/reqresData/bodyForRegister.json");

        Response response = reqresSteps.registerUser(body, 200);

        String id = response.jsonPath().getString("id");
        String token = response.jsonPath().getString("token");

        assertThat(id)
                .as("User id must be equal to: " + id)
                .isEqualTo("4");
        assertThat(token)
                .as("Token must not be null")
                .isNotBlank();
    }

    @ParameterizedTest()
    @ValueSource(strings = {"email", "password"})
    void unsuccessfulRegisterTest(String field) {
        String body = utils.getStringFromResource("/reqresData/bodyForRegister.json")
                .replace(field, "/" + field);

        Response response = reqresSteps.registerUser(body, 400);

        String actualError = response.jsonPath().getString("error");

        assertThat(actualError)
                .as("Error message must follow pattern 'Missing [%s]'", field)
                .matches("(?i).*Missing.*" + field + ".*");
    }

    @Test
    void successfulUpdateTest() {
        String body = utils.getStringFromResource("/reqresData/bodyForUpdate.json");

        String expectedName = utils.getFieldFromJsonString(body, "name");
        String expectedJob = utils.getFieldFromJsonString(body, "job");

        UserModel response = reqresSteps.updateUser(body, 2, 200);

        String actualName = response.getName();
        String actualJob = response.getJob();
        String date = response.getUpdatedAt();

        assertThat(actualName)
                .as("Name must be equal to: " + expectedName)
                .isEqualTo(expectedName);
        assertThat(actualJob)
                .as("Job must be equal to: " + expectedJob)
                .isEqualTo(expectedJob);
        assertThat(date)
                .as("Date must not be blank")
                .isNotBlank();
    }

    @Test
    void getUserTest() {
        int userId = 2;

        SingleUserResponse response = reqresSteps.getUser(userId, 200);

        assertThat(response.getData())
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", userId)
                .hasFieldOrPropertyWithValue("firstName", "Janet");
        assertThat(response.getSupport()).isNotNull();
    }

    @Test
    void getNullUserTest() {
        int userId = 23;

        SingleUserResponse response = reqresSteps.getUser(userId, 404);
        assertThat(response.getData()).isNull();
        assertThat(response.getSupport()).isNull();
    }

    @Test
    void getUsersTest() {
        int page = 2;
        UserListResponse response = reqresSteps.getUsers(page, 200);

        assertThat(response.getPage()).isEqualTo(page);
        assertThat(response.getData())
                .as("The data must not be empty")
                .isNotNull()
                .isNotEmpty();

        response.getData()
                .forEach(user -> {
                    assertThat(user)
                            .as("User with ID %s has all required fields", user.getId())
                            .hasNoNullFieldsOrProperties();
                });
    }

    @Test
    void getEmptyUsersTest() {
        int page = 23;
        UserListResponse response = reqresSteps.getUsers(page, 200);

        assertThat(response.getPage()).isEqualTo(page);
        assertThat(response.getData())
                .as("The data must be empty")
                .isNullOrEmpty();
    }

    @Test
    void deleteUserTest() {
        int userId = 2;
        Response response = reqresSteps.deleteUser(userId, 204);

        assertThat(response.getBody().asString())
                .as("Response body must be empty")
                .isEmpty();
    }
}

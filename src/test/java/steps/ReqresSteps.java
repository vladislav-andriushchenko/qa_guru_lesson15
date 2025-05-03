package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.SingleUserResponse;
import models.UserListResponse;
import models.UserModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.BaseSpec.responseSpec;

public class ReqresSteps {

    private final String REGISTER_URL = "/register";
    private final String USER_URL = "/users";



    public Response registerUser(String body, Integer statusCode) {
        return given(requestSpec)
                .body(body)
                .when()
                .post(REGISTER_URL)
                .then()
                .statusCode(statusCode)
                .spec(responseSpec)
                .extract().response();
    }

    @Step("Update a user with id {userId}")
    public UserModel updateUser(String body, Integer userId, Integer statusCode) {
        return given(requestSpec)
                .body(body)
                .when()
                .put(USER_URL + "/" + userId)
                .then()
                .statusCode(statusCode)
                .spec(responseSpec)
                .extract()
                .as(UserModel.class);
    }

    @Step("Get single user with id {userId}")
    public SingleUserResponse getUser(Integer userId, Integer statusCode) {
        return given(requestSpec)
                .when()
                .get(USER_URL + "/" + userId)
                .then()
                .statusCode(statusCode)
                .spec(responseSpec)
                .extract()
                .as(SingleUserResponse.class);
    }

    @Step("Get list of users from page {page}")
    public UserListResponse getUsers(Integer page, Integer statusCode) {
        return given(requestSpec)
                .queryParam("page", page)
                .when()
                .get(USER_URL)
                .then()
                .statusCode(statusCode)
                .spec(responseSpec)
                .extract()
                .as(UserListResponse.class);
    }

    @Step("Delete user with id {userId}")
    public Response deleteUser(int userId, Integer statusCode) {
        return given(requestSpec)
                .when()
                .delete(USER_URL + "/" + userId)
                .then()
                .statusCode(statusCode)
                .spec(responseSpec)
                .extract().response();
    }
}

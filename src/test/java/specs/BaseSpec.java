package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

public class BaseSpec {

    private static final String API_KEY = "reqres-free-v1";

    public static RequestSpecification requestSpec = with()
            .filter(withCustomTemplates())
            .header("x-api-key", API_KEY)
            .log().all()
            .contentType(JSON)
            .baseUri("https://reqres.in")
            .basePath("/api");

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .build();

    public static ResponseSpecification getResponseWithStatusCode(int statusCode) {
        return new ResponseSpecBuilder()
                .log(ALL)
                .expectStatusCode(statusCode)
                .build();
    }
}
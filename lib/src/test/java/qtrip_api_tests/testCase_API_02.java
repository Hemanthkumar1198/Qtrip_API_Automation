package qtrip_api_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class testCase_API_02 {

    @Test(testName = "TestCase02")
    public void cities() {
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1/";
        
        Response resp = given()
                .queryParam("q", "beng")
                .header("Content-Type", "application/json")
                .log().all()
                .when()
                .get("cities");
        
        System.out.println("Response Status Code: " + resp.getStatusCode());
        Assert.assertEquals(resp.getStatusCode(), 200);
        
        String responseBody = resp.body().asPrettyString();
        System.out.println("Response Body:");
        System.out.println(responseBody);
        
        resp.then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].description", containsString("100+ Places"));
    }
}

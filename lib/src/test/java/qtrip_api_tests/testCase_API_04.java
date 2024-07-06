package qtrip_api_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.UUID;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class testCase_API_04 {

    String email;
    String password;

    @Test(groups = {"API Tests"})
    public void registerDuplicateUser() {
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1/";
        
        // Register the first user
        JSONObject obj = new JSONObject();
        email = "User" + UUID.randomUUID().toString() + "@gmail.com";
        password = UUID.randomUUID().toString();
        obj.put("email", email);
        obj.put("password", password);
        obj.put("confirmpassword", password);

        Response resp1 = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(obj.toString())
                .log().all()
                .when()
                .post("register");

        System.out.println(resp1.body().asPrettyString());
        Assert.assertEquals(resp1.getStatusCode(), 201);

        // Attempt to register the same user again
        Response resp2 = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(obj.toString())
                .log().all()
                .when()
                .post("register");

        System.out.println(resp2.body().asPrettyString());
        Assert.assertEquals(resp2.getStatusCode(), 400); // Assuming 400 Bad Request for duplicate
        String errorMessage = resp2.getBody().jsonPath().getString("message");
        Assert.assertNotNull(errorMessage);
        Assert.assertTrue(errorMessage.contains("Email already exists"));
    }
}

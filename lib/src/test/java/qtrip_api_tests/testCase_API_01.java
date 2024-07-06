package qtrip_api_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

public class testCase_API_01 {

    String email;
    String password;
    String token;
    String id;

    @Test(testName = "TestCase01")
    public void registerAndLogin() {
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1/";

        // Register a new user
        JSONObject obj = new JSONObject();
        email = "User" + UUID.randomUUID().toString() + "@gmail.com";
        password = UUID.randomUUID().toString();
        obj.put("email", email);
        obj.put("password", password);
        obj.put("confirmpassword", password);

        Response resp = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(obj.toString())
                .log().all()
                .when()
                .post("register");

        System.out.println("Registration Response:");
        System.out.println(resp.body().asPrettyString());
        Assert.assertEquals(resp.getStatusCode(), 201);

        // Login with the registered user
        obj = new JSONObject();
        obj.put("email", email);
        obj.put("password", password);

        resp = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(obj.toString())
                .log().all()
                .when()
                .post("login");

        System.out.println("Login Response:");
        System.out.println(resp.body().asPrettyString());
        Assert.assertEquals(resp.getStatusCode(), 201);
        Assert.assertTrue(resp.getBody().jsonPath().getBoolean("success"));
        token = resp.getBody().jsonPath().getString("data.token");
        id = resp.getBody().jsonPath().getString("data.id");
        Assert.assertNotNull(token);
        Assert.assertNotNull(id);
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

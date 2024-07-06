package qtrip_api_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class testCase_API_03 {

    String token;
    String id;

    @Test(testName = "TestCase03")
    public void testcase() {
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath = "/api/v1/";

        testCase_API_01 test1 = new testCase_API_01();
        test1.registerAndLogin();
        id = test1.getId();
        token = test1.getToken();

        JSONObject obj = new JSONObject();
        obj.put("userId", id);
        obj.put("name", "testuser");
        obj.put("date", "2024-07-09");
        obj.put("person", "1");
        obj.put("adventure", "2447910730");

        System.out.println("Token: " + token);

        // Make a reservation
        Response resp = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(obj.toString())
                .log().all()
                .when()
                .post("reservations/new");

        System.out.println(resp.body().asPrettyString());
        Assert.assertEquals(resp.getStatusCode(), 200);

        // Verify the booking
        Response getReservationsResponse = RestAssured.given()
                .queryParam("id", id)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("reservations");

        System.out.println(getReservationsResponse.body().asPrettyString());
        Assert.assertEquals(getReservationsResponse.getStatusCode(), 200);

        String responseBody = getReservationsResponse.getBody().asString();
        System.out.println("Response Body: " + responseBody);

        // Parse the response as a JSONArray
        JSONArray reservations = new JSONArray(responseBody);
        Assert.assertTrue(reservations.length() > 0);

        JSONObject reservation = reservations.getJSONObject(0);
        Assert.assertEquals(reservation.getString("userId"), id);
        Assert.assertEquals(reservation.getString("name"), "Testuser");
        Assert.assertEquals(reservation.getString("date"), "2024-07-09");
        Assert.assertEquals(reservation.getString("person"), "1");
        Assert.assertEquals(reservation.getString("adventure"), "2447910730");
    }
}

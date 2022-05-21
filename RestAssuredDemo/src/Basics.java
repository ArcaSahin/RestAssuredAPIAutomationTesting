import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import Files.ReUsableMethods;
import Files.payload;

public class Basics {

	public static void main(String[] args) {
		// validate if Add Place API is working as expected
		
		// given - all input details
		// when - submit the API
		// then -  validate the response
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		RestAssured.useRelaxedHTTPSValidation();
		
		// post place
		
		String response = given().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(payload.AddPlace()).when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);
		
		JsonPath js = new JsonPath(response); // for parsing json
		String placeId = js.getString("place_id");
		
		System.out.println(placeId);
		
		// update place
		
		String newAddress = "Besiktas, Turkiye";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json").body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}").when().put("maps/api/place/update/json").then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		// get place
		
		String getPlaceResponse =  given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		//JsonPath js2 = new JsonPath(getPlaceResponse); // I closed this code piece since I created a class for JsonPath
		
		JsonPath js2 = ReUsableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js2.getString("address");
		
		System.out.println(actualAddress);
		
		// Cucumber Junit or TestNG to assert both values. I choose to go with TestNG by adding the jar manually
		
		Assert.assertEquals(actualAddress, newAddress);
		
		//Add place -> Update Place with new Address -> Get Place to validate if new address is present in response //3 APIs needed
		

	}

}

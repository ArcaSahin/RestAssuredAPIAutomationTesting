package Files;

import io.restassured.path.json.JsonPath;

public class ReUsableMethods {

	public static JsonPath rawToJson(String response) {
		
		JsonPath js2 = new JsonPath(response);
		return js2;
	}
}

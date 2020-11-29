package capstone.backend.api;

import capstone.backend.api.utils.RestUtils;
import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.http.ContentType;
import org.junit.Before;

public class BaseClass {

     String token = "";
     @Before
     public void setup() {
	  	// // Test Setup
	  	RestUtils.setBaseURI("http://bluemarble97.com"); // Setup Base URI
	  	RestUtils.setPort(8081);
	  	RestUtils.setBasePath("api"); // Setup Base Path
	  	RestUtils.setContentType(ContentType.JSON); // Setup Content Type
	  	String requestBody = "{\"email\" : \"namnhse05817@fpt.edu.vn\" , \"password\" : \"123456789\"} ";

	  	token = given().contentType(ContentType.JSON)
	  	.body(requestBody).when().post("auth/signin/")
	  	.jsonPath().get("data.jwtToken");
	  }

}

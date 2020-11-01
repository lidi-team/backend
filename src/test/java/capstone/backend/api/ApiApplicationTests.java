package capstone.backend.api;
import  capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.utils.RestUtils;
import java.util.*;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.json.simple.JSONObject;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Header;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

import org.testng.Assert;
import com.jayway.restassured.RestAssured;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@SpringBootTest
@AutoConfigureMockMvc
class ApiApplicationTests {

	private CommonProperties commonProperties;

	String token = "";
	// String email = commonProperties.getEmail();
	// String password = commonProperties.getPassword();
	@BeforeSuite
	public void setup() {
		// Test Setup
		RestUtils.setBaseURI("http://bluemarble97.com:8081"); // Setup Base URI
		RestUtils.setBasePath("api"); // Setup Base Path
		RestUtils.setContentType(ContentType.JSON); // Setup Content Type
		String requestBody = "{\"email\" : \"sontung199x@gmail.com\" , \"password\" : \"123445\"} ";
		
		token = given().contentType(ContentType.JSON)
		.body(requestBody).when().post("auth/signin/")
		.jsonPath().get("data.jwtToken");
	}

	@Test
	public void submitForm() {
		Header headers = new Header("Authorization", "lidi " +token);
		given().header(headers).when()
		.get("http://bluemarble97.com:8081/api/objective/key_result/23/")
		.then()
		.assertThat()
		.body("message", equalTo("Thành công"))
		.body("code", equalTo(200))
		.body(matchesJsonSchemaInClasspath("jsonschema.json"));
	}

}

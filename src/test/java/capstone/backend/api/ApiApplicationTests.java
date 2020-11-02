package capstone.backend.api;
import  capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.utils.RestUtils;
import org.testng.annotations.BeforeSuite;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Header;

import static org.hamcrest.Matchers.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiApplicationTests extends  BaseClass{

	// @Test
	// public void submitForm() {
	// 	Header headers = new Header("Authorization", "lidi " +token);
	// 	given().header(headers).when()
	// 	.get("objective/key_result/23/")
	// 	.then()
	// 	.assertThat()
	// 	.body("message", equalTo("Thành công"))
	// 	.body("code", equalTo(200))
	// 	.body(matchesJsonSchemaInClasspath("jsonschema.json"));
	// }

	// @Test
	// public void submitForm2() {
	// 	Header headers = new Header("Authorization", "lidi " +token);
	// 	given().header(headers).when()
	// 	.get("objective/parent-okr/4")
	// 	.then()
	// 	.assertThat()
	// 	.body("message", equalTo("Thành công"))
	// 	.body("code", equalTo(200));
	// }
}

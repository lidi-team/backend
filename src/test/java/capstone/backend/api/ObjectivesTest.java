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
public class ObjectivesTest extends  BaseClass{

	// @Test
	// public void T01() {
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
	// public void T02() {
	// 	Header headers = new Header("Authorization", "lidi " +token);
	// 	given().header(headers).when()
	// 	.get("objective/key_result/22/")
	// 	.then()
	// 	.assertThat()
	// 	.body("message", equalTo("Thành công"))
	// 	.body("code", equalTo(200))
	// 	.body(matchesJsonSchemaInClasspath("jsonschema.json"));
	// }
	// @Test
	// public void T03() {
	// 	Header headers = new Header("Authorization", "lidi " +token);
	// 	given().header(headers).when()
	// 	.get("objective/key_result/1")
	// 	.then()
	// 	.assertThat()
	// 	.body("message", equalTo("Thành công"))
	// 	.body("code", equalTo(200));
	// }
	// @Test
	// public void T04() {
	// 	Header headers = new Header("Authorization", "lidi " +token);
	// 	given().header(headers).when()
	// 	.get("objective/key_result/-1")
	// 	.then()
	// 	.assertThat()
	// 	.body("message", equalTo("Không tìm thấy dữ liệu"))
	// 	.body("code", equalTo(404))
	// 	.body(matchesJsonSchemaInClasspath("404ErrorSchema.json"));
	// }
	// @Test
	// public void T05() {
	// 	Header headers = new Header("Authorization", "lidi " +token);
	// 	given().header(headers).when()
	// 	.get("objective/key_result/0")
	// 	.then()
	// 	.assertThat()
	// 	.body("message", equalTo("Không tìm thấy dữ liệu"))
	// 	.body("code", equalTo(404))
	// 	.body(matchesJsonSchemaInClasspath("404ErrorSchema.json"));
	// }
	// @Test
	// public void T06() {
	// 	Header headers = new Header("Authorization", "lidi " +token);
	// 	given().header(headers).when()
	// 	.get("objective/key_result/sdf")
	// 	.then()
	// 	.assertThat()
	// 	.body("status", equalTo(400))
	// 	.body("error", equalTo("Bad Request"))
	// 	.body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));

	// }
	// @Test
	// public void T07() {
	// 	Header headers = new Header("Authorization", "lidi " +token);
	// 	given().header(headers).when()
	// 	.get("objective/key_result/.+342as")
	// 	.then()
	// 	.assertThat()
	// 	.body("status", equalTo(400))
	// 	.body("error", equalTo("Bad Request"))
	// 	.body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));
	// }
	// @Test
	// public void T08() {
	// 	Header headers = new Header("Authorization", "lidi " +token);
	// 	given().header(headers).when()
	// 	.get("objective/key_result/....")
	// 	.then()
	// 	.assertThat()
	// 	.body("status", equalTo(400))
	// 	.body("error", equalTo("Bad Request"))
	// 	.body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));

	// }
}

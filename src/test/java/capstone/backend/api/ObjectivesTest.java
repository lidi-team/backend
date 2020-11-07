package capstone.backend.api;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.utils.RestUtils;
import capstone.backend.api.dto.*;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import static org.hamcrest.Matchers.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ObjectivesTest extends BaseClass {

	@Test
	public void T00() {
		try {
			JSONObject request = new JSONObject();
			// List<JSONObject> list = new ArrayList();
			JSONObject kr1 = new JSONObject();
			kr1.put("id", null);
			kr1.put("content", "Get 10 milions");
			kr1.put("parentId", 25);
			kr1.put("startValue", 0);
			kr1.put("targetValue", 10);
			kr1.put("valueObtained", 0);
			kr1.put("measureUnitId", 2);
			kr1.put("reference", "{'link': 'workflow/aws.com'}");

			JSONObject kr2 = new JSONObject();
			kr2.put("id", null);
			kr2.put("content", "Finish in 60 days");
			kr2.put("parentId", 27);
			kr2.put("startValue", 0);
			kr2.put("targetValue", 60);
			kr2.put("valueObtained", 15);
			kr2.put("measureUnitId", 4);
			kr2.put("reference", "{'link': 'workflow/aws.com'}");

			// list.add(kr1);
			// list.add(kr2);
			JSONArray array = new JSONArray();
			array.put(kr1);
			array.put(kr2);
			JSONArray array2 = new JSONArray();
			request.put("id", null);
			request.put("title", "Finish define SRS");
			request.put("projectId", 2);
			request.put("parentId", 32);
			request.put("type", "3");
			request.put("weight", 4);
			request.put("cycleId", 3);
			request.put("alignmentObjectives", array2);
			request.put("keyResults", array);
			// request.put("")
			Header headers = new Header("Authorization", "lidi " + token);
			Response response = given()
			.contentType("application/json; charset=UTF-8")
			.header(headers).body(request.toString()).when().post("objective/add");
			System.out.println(response.statusCode());
			System.out.println(response.asString());
			System.out.println(response.getBody().asString());
			System.out.println(response.statusLine());
			int statusCode = response.getStatusCode();
			Assert.assertEquals(statusCode, 200);
		} catch (Exception e) {

		}
	}

	@Test
	public void T01() {
		Header headers = new Header("Authorization", "lidi " + token);
		given().header(headers).when().get("objective/key_result/23/").then().assertThat()
				.body("message", equalTo("Thành công")).body("code", equalTo(200))
				.body(matchesJsonSchemaInClasspath("jsonschema.json"));
	}

	@Test
	public void T02() {
		Header headers = new Header("Authorization", "lidi " + token);
		given().header(headers).when().get("objective/key_result/22/").then().assertThat()
				.body("message", equalTo("Thành công")).body("code", equalTo(200))
				.body(matchesJsonSchemaInClasspath("jsonschema.json"));
	}

	@Test
	public void T03() {
		Header headers = new Header("Authorization", "lidi " + token);
		given().header(headers).when().get("objective/key_result/1").then().assertThat()
				.body("message", equalTo("Thành công")).body("code", equalTo(200));
	}

	@Test
	public void T04() {
		Header headers = new Header("Authorization", "lidi " + token);
		given().header(headers).when().get("objective/key_result/-1").then().assertThat()
				.body("message", equalTo("Không tìm thấy dữ liệu")).body("code", equalTo(404))
				.body(matchesJsonSchemaInClasspath("404ErrorSchema.json"));
	}

	@Test
	public void T05() {
		Header headers = new Header("Authorization", "lidi " + token);
		given().header(headers).when().get("objective/key_result/0").then().assertThat()
				.body("message", equalTo("Không tìm thấy dữ liệu")).body("code", equalTo(404))
				.body(matchesJsonSchemaInClasspath("404ErrorSchema.json"));
	}

	@Test
	public void T06() {
		Header headers = new Header("Authorization", "lidi " + token);
		given().header(headers).when().get("objective/key_result/sdf").then().assertThat().body("status", equalTo(400))
				.body("error", equalTo("Bad Request")).body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));

	}

	@Test
	public void T07() {
		Header headers = new Header("Authorization", "lidi " + token);
		given().header(headers).when().get("objective/key_result/.+342as").then().assertThat()
				.body("status", equalTo(400)).body("error", equalTo("Bad Request"))
				.body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));
	}

	@Test
	public void T08() {
		Header headers = new Header("Authorization", "lidi " + token);
		given().header(headers).when().get("objective/key_result/....").then().assertThat().body("status", equalTo(400))
				.body("error", equalTo("Bad Request")).body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));

	}
}

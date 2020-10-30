package capstone.backend.api;

import capstone.backend.api.utils.RestUtils; 

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

import org.testng.Assert;
import com.jayway.restassured.RestAssured;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
@SpringBootTest
@AutoConfigureMockMvc
class ApiApplicationTests {

	//Setup moi truong test
	@BeforeSuite
    public void setup (){
        //Test Setup
        RestUtils.setBaseURI("http://bluemarble97.com:8081"); //Setup Base URI
        RestUtils.setBasePath("api"); //Setup Base Path
		RestUtils.setContentType(ContentType.JSON); //Setup Content Type
		RestUtils.createGetQueryPath();
	}
	
	// Test trong truong hop so sanh 2 ket qua cuoi cung
	@Test
	public void test1() throws Exception {
		Assert.assertEquals("Video Size is not equal to 4", "ab");
		Assert.assertTrue(false, "abc");
		Assert.assertEquals(34, 23);
	}

	// Test Response API tra ve
	@Test
	public void test4() throws Exception {
		//usecase 1
		given().
			when().
				get("/test/teacherRegister").
			then().
				assertThat().
				body(containsString("hoang5.com"));

		//usecase 2
		get("http://bluemarble97.com:8081/api/test/teacherRegister").then().assertThat()
			.body(matchesJsonSchemaInClasspath("pattern.json"));
		
		//usecase 3
		given().
			when().
				get("/test/teacherRegister").
			then().
				assertThat().
				body("password", equalTo("123445")).
				body("gender", equalTo(2));

	}

}

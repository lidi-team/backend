package capstone.backend.api;

import com.jayway.restassured.response.Header;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static com.jayway.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiApplicationTests extends BaseClass {

//     @Test
//     public void submitForm() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         given().header(headers).when()
//                 .get("objective/key_result/23/")
//                 .then()
//                 .assertThat()
//                 .body("message", equalTo("Thành công"))
//                 .body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("jsonschema.json"));
//     }
//
//     @Test
//     public void submitForm2() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         given().header(headers).when()
//                 .get("objective/parent-okr/4")
//                 .then()
//                 .assertThat()
//                 .body("message", equalTo("Thành công"))
//                 .body("code", equalTo(200));
//     }
}

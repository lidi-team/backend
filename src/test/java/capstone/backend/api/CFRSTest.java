// package capstone.backend.api;

// import capstone.backend.api.configuration.CommonProperties;
// import capstone.backend.api.utils.RestUtils;
// import capstone.backend.api.dto.*;

// import org.testng.Assert;
// import org.testng.annotations.BeforeSuite;
// import org.json.JSONArray;
// import org.json.JSONObject;
// import org.junit.Test;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;

// import static com.jayway.restassured.RestAssured.*;
// import com.jayway.restassured.http.ContentType;
// import com.jayway.restassured.response.Header;
// import com.jayway.restassured.response.Response;
// import static org.hamcrest.Matchers.*;

// import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
// import java.util.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class CFRSTest extends BaseClass {

//     // Test POST request: create new Objective
//     // @Test
//     // public void T00() {
//     // try {
//     // JSONObject request = new JSONObject();
//     // // List<JSONObject> list = new ArrayList();
//     // JSONObject kr1 = new JSONObject();
//     // kr1.put("id", null);
//     // kr1.put("content", "Get 10 milions");
//     // kr1.put("parentId", 25);
//     // kr1.put("startValue", 0);
//     // kr1.put("targetValue", 10);
//     // kr1.put("valueObtained", 0);
//     // kr1.put("measureUnitId", 2);
//     // kr1.put("reference", "{'link': 'workflow/aws.com'}");

//     // JSONObject kr2 = new JSONObject();
//     // kr2.put("id", null);
//     // kr2.put("content", "Finish in 60 days");
//     // kr2.put("parentId", 27);
//     // kr2.put("startValue", 0);
//     // kr2.put("targetValue", 60);
//     // kr2.put("valueObtained", 15);
//     // kr2.put("measureUnitId", 4);
//     // kr2.put("reference", "{'link': 'workflow/aws.com'}");

//     // // list.add(kr1);
//     // // list.add(kr2);
//     // JSONArray array = new JSONArray();
//     // array.put(kr1);
//     // array.put(kr2);
//     // JSONArray array2 = new JSONArray();
//     // request.put("id", null);
//     // request.put("title", "Finish define SRS");
//     // request.put("projectId", 2);
//     // request.put("parentId", 32);
//     // request.put("type", "3");
//     // request.put("weight", 4);
//     // request.put("cycleId", 3);
//     // request.put("alignmentObjectives", array2);
//     // request.put("keyResults", array);
//     // // request.put("")
//     // Header headers = new Header("Authorization", "lidi " + token);
//     // Response response = given()
//     // .contentType("application/json; charset=UTF-8")
//     // .header(headers).body(request.toString()).when().post("objective/add");
//     // Assert.assertEquals(response.statusCode(), 200);
//     // } catch (Exception e) {

//     // }
//     // }

//     // //Test GET request: get list of waiting feedbacks
//     @Test
//     public void T01() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         int page = 1;
//         int limit = 10;
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("message", equalTo("Thành công")).body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_getListWaiting_Schema.json"));
//     }

//     @Test
//     public void T02() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         int page = 2;
//         int limit = 10;
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("message", equalTo("Thành công")).body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_getListWaiting_Schema.json"));
//     }

//     @Test
//     public void T03() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "a";
//         int limit = 10;
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("status", equalTo(400)).body("error", equalTo("Bad Request"))
//                 .body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));
//     }

//     @Test
//     public void T04() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "a";
//         String limit = "b";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("status", equalTo(400)).body("error", equalTo("Bad Request"))
//                 .body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));
//     }

//     @Test
//     public void T05() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "123ag";
//         String limit = "123ag";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("status", equalTo(400)).body("error", equalTo("Bad Request"))
//                 .body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));
//     }

//     @Test
//     public void T06() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String limit = "123ag";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("status", equalTo(400)).body("error", equalTo("Bad Request"))
//                 .body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));

//     }

//     @Test
//     public void T07() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "10";
//         String limit = "123_3";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("status", equalTo(400)).body("error", equalTo("Bad Request"))
//                 .body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));
//     }

//     @Test
//     public void T08() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "10";
//         String limit = "-2";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("code", equalTo(402)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T09() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "10";
//         String limit = "-1";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("code", equalTo(402)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T10() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "-1";
//         String limit = "-1";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("code", equalTo(402)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T11() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "0";
//         String limit = "-1";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("code", equalTo(402)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T12() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "0";
//         String limit = "-2";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("code", equalTo(402)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T13() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "-1";
//         String limit = "0";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("code", equalTo(402)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T14() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "-2";
//         String limit = "0";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("code", equalTo(402)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T15() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "-1000";
//         String limit = "0";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("code", equalTo(402)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T16() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "-1000";
//         String limit = "-1000";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("code", equalTo(402)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T65() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "123_3";
//         String limit = "1000";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("status", equalTo(400)).body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));

//     }

//     @Test
//     public void T66() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1000";
//         String limit = "1000";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("code", equalTo(404)).body(matchesJsonSchemaInClasspath("404ErrorSchema.json"));

//     }

//     @Test
//     public void T67() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "ad_3*";
//         String limit = "-1000";
//         given().header(headers).when().get("cfrs/list-waiting?page=" + page + "&limit=" + limit + "").then()
//                 .assertThat().body("status", equalTo(400)).body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));

//     }
//     // GET LIST HISTORY CFRS

//     @Test
//     public void T17() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "10";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T18() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "2";
//         String cycleId = "3";
//         String limit = "10";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T19() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "10";
//         String cycleId = "3";
//         String limit = "10";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T20() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1000";
//         String cycleId = "3";
//         String limit = "10";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T21() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "-1000";
//         String cycleId = "3";
//         String limit = "10";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402));

//     }

//     @Test
//     public void T22() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "10000";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T23() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "-1000";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402));

//     }

//     @Test
//     public void T24() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "0";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T25() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "-1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T26() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "-2";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T27() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "-10000000";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T28() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "-1";
//         String cycleId = "3";
//         String limit = "-1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T29() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "-1000";
//         String cycleId = "3";
//         String limit = "-1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T30() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "-1000";
//         String cycleId = "3";
//         String limit = "-1000";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T31() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "0";
//         String limit = "-1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T32() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "1";
//         String limit = "-1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T33() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "2";
//         String limit = "-1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T34() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "-1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T35() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "100000";
//         String limit = "1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(404));

//     }

//     @Test
//     public void T36() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "-1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T37() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "-1000";
//         String cycleId = "3";
//         String limit = "-1000";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T38() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "0";
//         String limit = "1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T39() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "-1";
//         String limit = "1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T40() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "-2";
//         String limit = "1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T41() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "2";
//         String limit = "1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T42() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "1";
//         String limit = "1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T43() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T44() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "2";
//         String type = "0";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T45() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "2";
//         String type = "1";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T46() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "2";
//         String limit = "1";
//         String type = "2";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("CFRS_history_Schema.json"));

//     }

//     @Test
//     public void T47() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "1";
//         String type = "100000000";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T48() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String page = "1";
//         String cycleId = "3";
//         String limit = "1";
//         String type = "abcd";
//         given().header(headers).when()
//                 .get("cfrs/history?page=" + page + "&limit=" + limit + "&cycleId=" + cycleId + "&type=" + type + "")
//                 .then().assertThat().body("status", equalTo(400))
//                 .body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));

//     }

//     // GET STAR OF USER BY CYCLEID

//     @Test
//     public void T49() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String cycleId = "1";
//         given().header(headers).when().get("cfrs/user-star?cycleId=" + cycleId + "").then().assertThat().body("code",
//                 equalTo(200));
//     }

//     @Test
//     public void T50() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String cycleId = "2";
//         given().header(headers).when().get("cfrs/user-star?cycleId=" + cycleId + "").then().assertThat().body("code",
//                 equalTo(200));

//     }

//     @Test
//     public void T51() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String cycleId = "0";
//         given().header(headers).when().get("cfrs/user-star?cycleId=" + cycleId + "").then().assertThat().body("code",
//                 equalTo(200));

//     }

//     @Test
//     public void T52() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String cycleId = "3";
//         given().header(headers).when().get("cfrs/user-star?cycleId=" + cycleId + "").then().assertThat().body("code",
//                 equalTo(200));

//     }

//     @Test
//     public void T53() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String cycleId = "-1";
//         given().header(headers).when().get("cfrs/user-star?cycleId=" + cycleId + "").then().assertThat()
//                 .body("code", equalTo(402)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T54() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String cycleId = "-2";
//         given().header(headers).when().get("cfrs/user-star?cycleId=" + cycleId + "").then().assertThat()
//                 .body("code", equalTo(402)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T68() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String cycleId = "abc";
//         given().header(headers).when().get("cfrs/user-star?cycleId=" + cycleId + "").then().assertThat()
//                 .body("status", equalTo(400)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T69() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String cycleId = "1000000";
//         given().header(headers).when().get("cfrs/user-star?cycleId=" + cycleId + "").then().assertThat()
//                 .body("code", equalTo(404)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T70() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String cycleId = "-1000000";
//         given().header(headers).when().get("cfrs/user-star?cycleId=" + cycleId + "").then().assertThat()
//                 .body("code", equalTo(404)).body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     // GET DETAIL CFR
//     @Test
//     public void T55() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String id = "-1";
//         given().header(headers).when().get("cfrs/detail/" + id + "").then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T56() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String id = "-2";
//         given().header(headers).when().get("cfrs/detail/" + id + "").then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T57() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String id = "0";
//         given().header(headers).when().get("cfrs/detail/" + id + "").then().assertThat().body("code", equalTo(404))
//                 .body(matchesJsonSchemaInClasspath("404ErrorSchema.json"));

//     }

//     @Test
//     public void T58() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String id = "1";
//         given().header(headers).when().get("cfrs/detail/" + id + "").then().assertThat().body("code", equalTo(404))
//                 .body(matchesJsonSchemaInClasspath("404ErrorSchema.json"));

//     }

//     @Test
//     public void T59() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String id = "2";
//         given().header(headers).when().get("cfrs/detail/" + id + "").then().assertThat().body("code", equalTo(404))
//                 .body(matchesJsonSchemaInClasspath("404ErrorSchema.json"));

//     }

//     @Test
//     public void T60() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String id = "-100000";
//         given().header(headers).when().get("cfrs/detail/" + id + "").then().assertThat().body("code", equalTo(402))
//                 .body(matchesJsonSchemaInClasspath("402ErrorSchema.json"));

//     }

//     @Test
//     public void T61() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String id = "100000";
//         given().header(headers).when().get("cfrs/detail/" + id + "").then().assertThat().body("code", equalTo(404))
//                 .body(matchesJsonSchemaInClasspath("404ErrorSchema.json"));

//     }

//     @Test
//     public void T62() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String id = "abc";
//         given().header(headers).when().get("cfrs/detail/" + id + "").then().assertThat().body("status", equalTo(400))
//                 .body(matchesJsonSchemaInClasspath("400ErrorSchema.json"));

//     }

//     @Test
//     public void T63() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String id = "3";
//         given().header(headers).when().get("cfrs/detail/" + id + "").then().assertThat().body("code", equalTo(200));

//     }

//     @Test
//     public void T64() {
//         Header headers = new Header("Authorization", "lidi " + token);
//         String id = "4";
//         given().header(headers).when().get("cfrs/detail/" + id + "").then().assertThat().body("code", equalTo(200));

//     }
// }

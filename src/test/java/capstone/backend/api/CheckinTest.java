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
// public class CheckinTest extends BaseClass {


// 	// GET LIST HISTORY BY OBJECTIVE ID
// 	@Test
// 	public void T00() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "22";
// 	  given().header(headers).when().get("checkin/history/"+id+"").then().assertThat()
// 		  .body("status", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getHistoryByObjectiveIDSchema.json"));
		  
// 	} 
// 	@Test
// 	public void T01() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "23";
// 	  given().header(headers).when().get("checkin/history/"+id+"").then().assertThat()
// 		  .body("status", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getHistoryByObjectiveIDSchema.json"));
		  
// 	} 
// 	@Test
// 	public void T02() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "24";
// 	  given().header(headers).when().get("checkin/history/"+id+"").then().assertThat()
// 		  .body("status", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getHistoryByObjectiveIDSchema.json"));
		  
// 	} 
// 	@Test
// 	public void T03() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "0";
// 	  given().header(headers).when().get("checkin/history/"+id+"").then().assertThat()
// 		  .body("status", equalTo(404));
		  
// 	} 
// 	@Test
// 	public void T04() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "-1";
// 	  given().header(headers).when().get("checkin/history/"+id+"").then().assertThat()
// 		  .body("status", equalTo(402));
		  
// 	} 
// 	@Test
// 	public void T05() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "-2";
// 	  given().header(headers).when().get("checkin/history/"+id+"").then().assertThat()
// 		  .body("status", equalTo(402));
		  
// 	} 
// 	@Test
// 	public void T06() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "-10";
// 	  given().header(headers).when().get("checkin/history/"+id+"").then().assertThat()
// 		  .body("status", equalTo(402));
		  
// 	} 
// 	@Test
// 	public void T07() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "10000000";
// 	  given().header(headers).when().get("checkin/history/"+id+"").then().assertThat()
// 		  .body("status", equalTo(404));
		  
// 	} 
// 	@Test
// 	public void T08() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "-100000000";
// 	  given().header(headers).when().get("checkin/history/"+id+"").then().assertThat()
// 		  .body("status", equalTo(404));
		  
// 	} 
// 	@Test
// 	public void T09() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "abcde";
// 	  given().header(headers).when().get("checkin/history/"+id+"").then().assertThat()
// 		  .body("status", equalTo(400));
		  
// 	} 

// 	// GET DETAIL CHECKIN BY OBJECTIVE ID
// 	@Test
// 	public void T10() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "1";
// 	  given().header(headers).when().get("checkin/objective/"+id+"").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getDetailCheckinByObjectiveIDSchema.json"));
		  
// 	} 
// 	@Test
// 	public void T11() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "2";
// 	  given().header(headers).when().get("checkin/objective/"+id+"").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getDetailCheckinByObjectiveIDSchema.json"));
		  
// 	} 
// 	@Test
// 	public void T12() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "0";
// 	  given().header(headers).when().get("checkin/objective/"+id+"").then().assertThat()
// 		  .body("code", equalTo(404));
		  
// 	} 
// 	@Test
// 	public void T13() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "-1";
// 	  given().header(headers).when().get("checkin/objective/"+id+"").then().assertThat()
// 		  .body("code", equalTo(402));
		  
// 	} 
// 	@Test
// 	public void T14() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "-2";
// 	  given().header(headers).when().get("checkin/objective/"+id+"").then().assertThat()
// 		  .body("code", equalTo(402));
		  
// 	} 
// 	@Test
// 	public void T15() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "-10";
// 	  given().header(headers).when().get("checkin/objective/"+id+"").then().assertThat()
// 		  .body("code", equalTo(402));
		  
// 	} 
// 	@Test
// 	public void T16() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "1000000";
// 	  given().header(headers).when().get("checkin/objective/"+id+"").then().assertThat()
// 		  .body("code", equalTo(404));
		  
// 	} 
// 	@Test
// 	public void T17() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "-10000000000";
// 	  given().header(headers).when().get("checkin/objective/"+id+"").then().assertThat()
// 		  .body("code", equalTo(402));
		  
// 	} 
// 	@Test
// 	public void T18() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "abcd";
// 	  given().header(headers).when().get("checkin/objective/"+id+"").then().assertThat()
// 		  .body("status", equalTo(400));
		  
// 	} 
// 	@Test
// 	public void T19() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "1_abe*";
// 	  given().header(headers).when().get("checkin/objective/"+id+"").then().assertThat()
// 		  .body("status", equalTo(400));
		  
// 	} 


// 	// GET DETAIL CHECKIN BY CHECKIN ID
// 	@Test
// 	public void T20() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "1";
// 	  given().header(headers).when().get("checkin/"+id+"").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getDetailCheckinByCheckinIDSchema.json"));

		  
// 	} 
// 	@Test
// 	public void T21() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "2";
// 	  given().header(headers).when().get("checkin/"+id+"").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getDetailCheckinByCheckinIDSchema.json"));

		  
// 	} 
// 	@Test
// 	public void T22() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "0";
// 	  given().header(headers).when().get("checkin/"+id+"").then().assertThat()
// 		  .body("code", equalTo(404));
		  
// 	} 
// 	@Test
// 	public void T23() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "-1";
// 	  given().header(headers).when().get("checkin/"+id+"").then().assertThat()
// 		  .body("code", equalTo(402));
		  
// 	} 
// 	@Test
// 	public void T24() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "-2";
// 	  given().header(headers).when().get("checkin/"+id+"").then().assertThat()
// 		  .body("code", equalTo(402));
		  
// 	} 
// 	@Test
// 	public void T25() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "10000000";
// 	  given().header(headers).when().get("checkin/"+id+"").then().assertThat()
// 		  .body("code", equalTo(404));
		  
// 	} 
// 	@Test
// 	public void T26() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "-1000000000";
// 	  given().header(headers).when().get("checkin/"+id+"").then().assertThat()
// 		  .body("code", equalTo(404));
		  
// 	} 
// 	@Test
// 	public void T27() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "abcde";
// 	  given().header(headers).when().get("checkin/"+id+"").then().assertThat()
// 		  .body("status", equalTo(400));
		  
// 	} 
// 	@Test
// 	public void T28() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "12abc";
// 	  given().header(headers).when().get("checkin/"+id+"").then().assertThat()
// 		  .body("status", equalTo(400));
		  
// 	} 
// 	@Test
// 	public void T29() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "abcde*d3";
// 	  given().header(headers).when().get("checkin/"+id+"").then().assertThat()
// 		  .body("status", equalTo(400));
		  
// 	} 
// 	@Test
// 	public void T30() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String id = "10*";
// 	  given().header(headers).when().get("checkin/"+id+"").then().assertThat()
// 		  .body("status", equalTo(400));
		  
// 	} 

// 	// GET LIST REQUEST CHECKIN
// 	@Test
// 	public void T31() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getListRequestCheckinSchema.json"));

// 	} 
// 	@Test
// 	public void T32() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "0";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getListRequestCheckinSchema.json"));

// 	} 
// 	@Test
// 	public void T33() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1000000000";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getListRequestCheckinSchema.json"));

// 	} 
// 	@Test
// 	public void T34() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "-1";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("code", equalTo(402));

// 	} 
// 	@Test
// 	public void T35() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "-2";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 	  .body("code", equalTo(402));

// 	} 
// 	@Test
// 	public void T36() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "-100000000000";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 	  .body("code", equalTo(402));

// 	} 
// 	@Test
// 	public void T37() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "abc";
// 	  String limit = "1";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("status", equalTo(400));

// 	} 
// 	@Test
// 	public void T38() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "135f*";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("status", equalTo(400));

// 	} 
// 	@Test
// 	public void T39() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "1000000";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getListRequestCheckinSchema.json"));

// 	} 
// 	@Test
// 	public void T40() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "0";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("code", equalTo(402));

// 	} 
// 	@Test
// 	public void T41() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "-1";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("code", equalTo(402));

// 	} 
// 	@Test
// 	public void T42() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "-2";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("code", equalTo(402));

// 	} 
// 	@Test
// 	public void T43() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "-1000000";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("code", equalTo(402));
// 	} 
// 	@Test
// 	public void T44() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "abcde";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 
// 	@Test
// 	public void T45() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "12ab";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 
// 	@Test
// 	public void T46() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "23*3";
// 	  String cycleId = "3";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 
// 	@Test
// 	public void T47() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "10";
// 	  String cycleId = "4";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+" ").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getListRequestCheckinSchema.json"));

// 	} 
// 	@Test
// 	public void T48() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "10";
// 	  String cycleId = "0";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(404));
// 	} 
// 	@Test
// 	public void T49() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "10";
// 	  String cycleId = "-1";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(402));
// 	} 
// 	@Test
// 	public void T50() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "10";
// 	  String cycleId = "-2";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(402));
// 	} 
// 	@Test
// 	public void T51() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "10";
// 	  String cycleId = "-10000000";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(402));
// 	} 
// 	@Test
// 	public void T52() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "10";
// 	  String cycleId = "abcd";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 
// 	@Test
// 	public void T53() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "10";
// 	  String cycleId = "124ab";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 
// 	@Test
// 	public void T54() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "10";
// 	  String cycleId = "124_4";
// 	  given().header(headers).when().get("checkin/checkin_request?page="+page+"&limit="+limit+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 

// 	// GET LIST INFERIORS
// 	@Test
// 	public void T55() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getListRequestCheckinSchema.json"));

// 	} 
// 	@Test
// 	public void T56() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "2";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getListRequestCheckinSchema.json"));

// 	} 
// 	@Test
// 	public void T57() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1000000";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getListRequestCheckinSchema.json"));

// 	} 
// 	@Test
// 	public void T58() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "0";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(200))
// 		  .body(matchesJsonSchemaInClasspath("Checkin_getListRequestCheckinSchema.json"));

// 	} 
// 	@Test
// 	public void T59() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "-1";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(402));
// 	} 
// 	@Test
// 	public void T60() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "-2";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(402));
// 	} 
// 	@Test
// 	public void T61() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "-1000000";
// 	  String limit = "1";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(402));
// 	} 
// 	@Test
// 	public void T62() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "abcde";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 
// 	@Test
// 	public void T63() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "124af";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 
// 	@Test
// 	public void T64() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "2234_43";
// 	  String limit = "10";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 
// 	@Test
// 	public void T65() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "0";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(404));
// 	} 
// 	@Test
// 	public void T66() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "-1";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(402));
// 	} 
// 	@Test
// 	public void T67() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "-2";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(402));
// 	} 
// 	@Test
// 	public void T68() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "-10000000";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("code", equalTo(402));
// 	} 
// 	@Test
// 	public void T69() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "abcd";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 
// 	@Test
// 	public void T70() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "1245ab";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 
// 	@Test
// 	public void T71() {
// 	  Header headers = new Header("Authorization", "lidi " + token);
// 	  String page = "1";
// 	  String limit = "124_32";
// 	  String cycleId = "3";
// 	  String projectId = "1";
// 	  given().header(headers).when().get("checkin/inferior?page="+page+"&limit="+limit+"&projectId="+projectId+"&cycleId="+cycleId+"").then().assertThat()
// 		  .body("status", equalTo(400));
// 	} 
	
// }

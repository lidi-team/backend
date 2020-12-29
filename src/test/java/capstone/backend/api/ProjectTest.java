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
// public class ProjectTest extends BaseClass{
    

//     // GET ALL AVAILABLE PROJECT OF CURRENT USER
//     @Test
// 	public void T00() {
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/available").then().assertThat()
// 				.body("message", equalTo("Thành công")).body("code", equalTo(200))
// 				.body(matchesJsonSchemaInClasspath("getAllCurrentProjectOfUserSchema.json"));
//     }
    
//     // GET PROJECT BY ID
//     // @Test
// 	// public void T01() {
//     //     String id = "1";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"").then().assertThat()
// 	// 			.body("message", equalTo("Thành công")).body("code", equalTo(200))
// 	// 			.body(matchesJsonSchemaInClasspath("getProjectByIdSchema.json"));
//     // }
//     // @Test
// 	// public void T02() {
//     //     String id = "2";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"").then().assertThat()
// 	// 			.body("message", equalTo("Thành công")).body("code", equalTo(200))
// 	// 			.body(matchesJsonSchemaInClasspath("getProjectByIdSchema.json"));
//     // }
//     // @Test
// 	// public void T03() {
//     //     String id = "0";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"").then().assertThat()
// 	// 			.body("code", equalTo(404));
//     // }
//     // @Test
// 	// public void T04() {
//     //     String id = "-1";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"").then().assertThat()
// 	// 			.body("code", equalTo(402));
//     // }
//     // @Test
// 	// public void T05() {
//     //     String id = "-2";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"").then().assertThat()
// 	// 			.body("code", equalTo(402));
//     // }
//     // @Test
// 	// public void T06() {
//     //     String id = "-100000";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"").then().assertThat()
// 	// 			.body("code", equalTo(402));
//     // }
//     // @Test
// 	// public void T07() {
//     //     String id = "100000";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"").then().assertThat()
// 	// 			.body("code", equalTo(404));
//     // }
//     @Test
// 	public void T08() {
//         String id = "abc";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"").then().assertThat()
// 				.body("status", equalTo(400));
//     }

//     // GET LIST PARENT PROJECT
//     @Test
// 	public void T09() {
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/parents").then().assertThat()
//                 .body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("getListParentProjectSchema.json"));

//     }

// 	// GET ALL STAFFS OF A PROJECT BY PROJECT ID
// 	@Test
// 	public void T10() {
// 		String id = "1";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/staff").then().assertThat()
//                 .body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("getAllStaffOfProjectByProjectIdSchema.json"));

// 	}
// 	@Test
// 	public void T11() {
// 		String id = "2";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/staff").then().assertThat()
//                 .body("code", equalTo(200))
//                 .body(matchesJsonSchemaInClasspath("getAllStaffOfProjectByProjectIdSchema.json"));

// 	}
// 	// @Test
// 	// public void T12() {
// 	// 	String id = "0";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"/staff").then().assertThat()
//     //             .body("code", equalTo(404));

// 	// }
// 	@Test
// 	public void T13() {
// 		String id = "-1";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/staff").then().assertThat()
//                 .body("code", equalTo(4001));
// 	}
// 	@Test
// 	public void T14() {
// 		String id = "-2";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/staff").then().assertThat()
//                 .body("code", equalTo(4001));
// 	}
// 	@Test
// 	public void T15() {
// 		String id = "10000000";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/staff").then().assertThat()
//                 .body("code", equalTo(4001));
// 	}
// 	@Test
// 	public void T16() {
// 		String id = "-10000000";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/staff").then().assertThat()
//                 .body("code", equalTo(4001));
// 	}
// 	@Test
// 	public void T17() {
// 		String id = "abcd";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/staff").then().assertThat()
//                 .body("status", equalTo(400));
//     }

// 	// GET LIST CANDIDATE TO ADD TO PROJECT
// 	@Test
// 	public void T18() {
// 		String id = "1";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/candidate").then().assertThat()
// 				.body("code", equalTo(200))
// 				.body(matchesJsonSchemaInClasspath("getListCandidateToAddToProjectSchema.json"));
// 	}
// 	@Test
// 	public void T19() {
// 		String id = "2";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/candidate").then().assertThat()
// 				.body("code", equalTo(200))
// 				.body(matchesJsonSchemaInClasspath("getListCandidateToAddToProjectSchema.json"));
// 	}
// 	// @Test
// 	// public void T20() {
// 	// 	String id = "0";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"/candidate").then().assertThat()
// 	// 			.body("code", equalTo(200));
// 	// 			}
// 	// @Test
// 	// public void T21() {
// 	// 	String id = "-1";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"/candidate").then().assertThat()
// 	// 			.body("code", equalTo(402));
// 	// 			}
// 	// @Test
// 	// public void T22() {
// 	// 	String id = "-2";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"/candidate").then().assertThat()
// 	// 			.body("code", equalTo(402));
// 	// }
// 	// @Test
// 	// public void T23() {
// 	// 	String id = "1000000000";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"/candidate").then().assertThat()
// 	// 			.body("code", equalTo(404));
// 	// }
// 	// @Test
// 	// public void T24() {
// 	// 	String id = "-10000000";
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/"+id+"/candidate").then().assertThat()
// 	// 			.body("code", equalTo(402))
// 	// 			.body(matchesJsonSchemaInClasspath("getListCandidateToAddToProjectSchema.json"));
// 	// }
// 	@Test
// 	public void T25() {
// 		String id = "abcde";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/candidate").then().assertThat()
// 				.body("status", equalTo(400));
// 	}
// 	@Test
// 	public void T26() {
// 		String id = "123ag";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/candidate").then().assertThat()
// 				.body("status", equalTo(400));
// 	}
// 	@Test
// 	public void T27() {
// 		String id = "123_43";
// 		Header headers = new Header("Authorization", "lidi " + token);
// 		given().header(headers).when().get("projects/"+id+"/candidate").then().assertThat()
// 				.body("status", equalTo(400));
//     }
//     // GET ALL PROJECT
//     // @Test
// 	// public void T01() {
// 	// 	Header headers = new Header("Authorization", "lidi " + token);
// 	// 	given().header(headers).when().get("projects/available").then().assertThat()
// 	// 			.body("message", equalTo("Thành công")).body("code", equalTo(200))
// 	// 			.body(matchesJsonSchemaInClasspath("getAllCurrentProjectOfUserSchema.json"));
// 	// }
	


    
// }

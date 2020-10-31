package capstone.backend.api;

// import static org.testng.annotations.Test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    public void test1() throws Exception {
//        this.mockMvc.perform(get("/api/test/teacherRegister")).andDo(print()).andExpect(status().isOk())
//                .andExpect(content().string(containsString("hoang5.com")));
//    }

    // @Test
    // public void test2() throws Exception {

    // 	this.mockMvc.perform(get("/api/test/studentRegister")).andDo(print()).andExpect(status().isOk())
    // 			.andExpect(content().string(containsString("mothaiba")));
    // }

    // @Test
    // public void test3() throws Exception {
    // 	this.mockMvc.perform(get("/api/test/studentRegister")).andDo(print()).andExpect(status().isOk())
    // 			.andExpect(content().string(containsString("mothaiba")))
    // 			// .andExpect(content().json("{\"email\":\"sontung199x@gmail.com\",\"password\":\"123445\",\"dob\":\"22/11/1998\",\"fullName\":Le
    // 			// Son
    // 			// Tung\",\"phoneNumber\":\"0342529999\",\"gender\":\"male\",\"roles\":[\"ROLE_EMPLOYEE\"]}"));
    // 			.andExpect(jsonPath("$.email").value("nguyenminhchau@gmail.com"))
    // 			.andExpect(jsonPath("$.password").value("mothaiba"))
    // 			.andExpect(jsonPath("$.fullName").value("Nguyen Minh Chau"))
    // 			.andExpect(jsonPath("$.phoneNumber").value("0369829999"))
    // 			.andExpect(jsonPath("$.gender").value("female"));
    // }

//    @Test
//    public void test4() throws Exception {
//        // InputStream is = new FileInputStream("./jsonschema.json");
//        // BufferedReader buf = new BufferedReader(new InputStreamReader(is));
//        // String line = buf.readLine();
//        // StringBuilder sb = new StringBuilder();
//        // while (line != null) {
//        // 	sb.append(line).append("\n");
//        // 	line = buf.readLine();
//        // }
//        // String fileAsString = sb.toString();
//
//        // JSONTokener schemaData = new JSONTokener(fileAsString);
//        // // JSONObject jsonschema = new JSONObject(schemaData);
//        // System.out.println(fileAsString);
//    }

}

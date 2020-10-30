package capstone.backend.api.utils;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
 
import java.util.*;
 
import static org.junit.Assert.assertEquals;
 
public class HelperMethods {

    public static void checkStatusIs200 (Response res) {
        assertEquals("Status Check Failed!", 200, res.getStatusCode());
    }
}
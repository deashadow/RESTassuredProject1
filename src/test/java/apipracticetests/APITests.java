package apipracticetests;

import com.google.common.io.Files;
import static io.restassured.RestAssured.*;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class APITests {
     Response response;

    @AfterMethod//not working ???
    public void recordFailure(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            var camera = (TakesScreenshot) response;
            File screenshot = camera.getScreenshotAs(OutputType.FILE);
            try {
                Files.move(screenshot, new File("resources/screenshots/" + result.getName() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void test1() {
        //store 'response' in a variable at Class level!!
         response = get("https://reqres.in/api/users?page=2");

        System.out.println("Response: " + response.asPrettyString());
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Body : " + response.asPrettyString());
        System.out.println("Time taken for response: " + response.getTime());
        System.out.println("Response Header: " + response.header("content-type"));

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }

     @Test
     void test2(){

        given().
                get("https://reqres.in/api/users?page=2").
                then().
                statusCode(200);

        }

    }


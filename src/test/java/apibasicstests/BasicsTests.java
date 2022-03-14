package apibasicstests;

import files.PayLoad;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BasicsTests {

    public static void main(String[] args) {

 //validate if AddPlace API is working as expected
        //Add place--> Update Place with New Address --> Get Place to validate if New Address
        //  is present in the response
  //Framework:::  this is basically all from Postman!!!
        // Given-- all input details
        // When -- Submit the API  --resource and http Method(GET, POST, etc)
        // Then -- Validate the response
        //use log().all() to document call(INPUT)
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        //extracting String to variable 'response'
        String response = given().log().all().queryParam("key", "qaclick123").
                header("Content-Type", "application/json")
                //this comes from new Java Class!!
                .body(PayLoad.AddPlace()).when().post("maps/api/place/add/json")
           //want to Validate codeStatus---  200, etc
            //use log().all() for OUTPUT!! this is from AddPlace response!!  (equalTo)this is a 'static package'--hamcrest!!
                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.18 (Ubuntu)").extract().response().asPrettyString();
           // printing String 'response'//Entire JSON response body in variable!!
        System.out.println(response);
          //use to extract (parsing) individual pieces of data from JSON body!!
        JsonPath js = new JsonPath(response);
        //store place_id in new variable!!!
        String placeID = js.getString("place_id");
        //pass variable to be printed!!
        System.out.println("Your place ID is: " + placeID);

        //Update Place--all comes from Postman!!
        //Create a new variable to store address!!
        String newAddress = "Summer Walk, Africa";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                           // USE 'placeID' variable!!! from previous add!!
                          //  use this Format!!! Concatenation to show variable
                .body("{\n" +
                        "\"place_id\":\"" + placeID + "\",\n" +
                        "\"address\":\"" + newAddress + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("maps/api/place/update/json")
                        .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));

        //  Get Place   Do not need to mention .header---  not sending any 'body'
        String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", placeID)
                .when().get("maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response().asPrettyString();
        //use to extract (parsing) individual pieces of data from JSON body!!
        JsonPath js1 = new JsonPath(getPlaceResponse);
        //store address in new variable!!!
        String actualAddress = js1.getString("address");
        System.out.println("Your address is: " + actualAddress);
        //Must have an assertion!!!  either JUNIT or TestNG
        Assert.assertEquals(actualAddress, newAddress);


    }
}

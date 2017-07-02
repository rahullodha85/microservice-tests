package runner.Steps.Common;

import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.Before;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;

public class CommonActions extends ScenarioBase {

    @Before()
    public void setup() {
        RestAssured.useRelaxedHTTPSValidation();
        cookies = new ArrayList();
        context = new HashMap<>();
    }

    @Given("^User (.+) signs in with valid (.+)$")
    public void getSignedInSessionID(String email, String password) throws Throwable {
        cookies.add("UserName=" + email);

        ValidatableResponse validatableResponse = RestAssured
                .given()
                    .log().all()
                    .header("content-type", "application/json")
                    .body(getLoginPostBody(email, password))
                .when()
                    .post(getServiceUrl() + "/account-service/sign-in-action")
                .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200);

        String jsessionid = validatableResponse.extract().cookie("JSESSIONID");
        cookies.add(String.format("JSESSIONID=%s", jsessionid));
    }

    public static String getLoginPostBody(String email, String pwd) {
        JsonObject root = new JsonObject();
        root.addProperty("username", email);
        root.addProperty("password", pwd);
        return root.toString();
    }

    @Given("^user has invalid JSessionID")
    public void setInvalidJSessionID() {
        for(int i = 0; i < cookies.size(); i++) {
            if (cookies.get(i).contains("JSESSIONID")) {
                cookies.set(i, "JSESSIONID=12345");
            }
        }
    }

    @Then("^user should get status (.+) and error (.+) in response")
    public void verifyCreatePaymentError(int status, String errorMsg){
        httpResponse
                .then()
                    .log().ifValidationFails()
                    .statusCode(status)
                    .body("errors[0].data", equalTo(errorMsg));
    }
}

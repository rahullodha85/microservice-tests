package cucumber.steps;

import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;

public class forgotPassword extends ScenarioBase {
    @When("^I forgot my password and enter an email that does not exist in the database$")
    public void enterEmailThatDoesNotExist() throws Throwable {

        JsonObject postBody = new JsonObject();
        postBody.addProperty("email", "iDontThinkThisEmail@Exists.com");

        httpResponse = RestAssured
                .given()
                    .log().all()
                    .header("Content-Type", "application/json")
                    .body(postBody.toString())
                .when()
                    .post(getServiceUrl() + "/account-service/forgot-password-action");
    }

    @Then("^User should receive a 200 success response$")
    public void user_should_receive_a_valid_SignIn_page_response() throws Throwable {
        httpResponse.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("response.results.success", is(true))
                .body("errors", is(Collections.emptyList()));
    }
}

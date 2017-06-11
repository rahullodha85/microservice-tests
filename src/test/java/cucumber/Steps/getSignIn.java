package cucumber.steps;

import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class getSignIn extends ScenarioBase {
    @When("^User sends Get SignIn page request$")
    public void user_sends_Get_SignIn_page_request() throws Throwable {
        httpResponse = RestAssured.when().get(getServiceUrl() + "/account-service/sign-in");
    }

    @Then("^User should receive a valid SignIn page response$")
    public void user_should_receive_a_valid_SignIn_page_response() throws Throwable {
        httpResponse.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("response.results", notNullValue())
                .body("errors", is(Collections.emptyList()));
    }
}

package runner.Steps;

import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import runner.Steps.Common.ScenarioBase;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class getRegister extends ScenarioBase {
    @When("^User sends Get Register Account Page request$")
    public void user_sends_Get_Register_Account_Page_request() throws Throwable {
        httpResponse = RestAssured.when().get(getServiceUrl() + "/account-service/register");
    }

    @Then("^User should receive a valid register account response$")
    public void user_should_receive_a_valid_register_account_response() throws Throwable {

        httpResponse.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("response.results", notNullValue())
                .body("errors", is(Collections.emptyList()));
    }
}

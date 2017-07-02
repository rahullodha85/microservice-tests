package runner.Steps;

import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import runner.Steps.Common.ScenarioBase;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class getPaymentMethod extends ScenarioBase {

    @When("^User checks account payment methods$")
    public void user_check_account_summary() throws Throwable {
        httpResponse = RestAssured
                .given()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/payment");
    }

    @Then("^User should get an account payment methods response$")
    public void user_should_get_an_account_summary_response() throws Throwable {
        httpResponse.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("response.results", notNullValue())
                .body("errors", is(Collections.emptyList()));
    }
}

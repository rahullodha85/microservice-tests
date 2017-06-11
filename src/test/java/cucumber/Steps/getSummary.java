package cucumber.steps;

import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class getSummary extends ScenarioBase {

    @When("^User check account summary$")
    public void user_check_account_summary() throws Throwable {
        httpResponse = RestAssured
                .given()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/summary");
    }

    @Then("^User should get an account summary response$")
    public void user_should_get_an_account_summary_response() throws Throwable {
        httpResponse.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("response.results", notNullValue())
                .body("errors", is(Collections.emptyList()));
    }
}

package cucumber.steps;

import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class getOrderHistory extends ScenarioBase {
    @When("^User checks account order history$")
    public void user_checks_account_order_history() throws Throwable {
        httpResponse = RestAssured
                    .given()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/order-history");
    }

    @Then("^User should get an account order history response$")
    public void user_should_get_an_account_order_history_response() throws Throwable {
        httpResponse.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("response.results", notNullValue())
                .body("errors", is(Collections.emptyList()));
    }
}

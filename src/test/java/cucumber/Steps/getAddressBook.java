package cucumber.steps;

import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class getAddressBook extends ScenarioBase {
    @When("^User checks (.+) address-book$")
    public void user_checks_account_address_book(String addressType) throws Throwable {

        httpResponse = RestAssured
                .given()
                    .header("Cookie", createCookieHeader())
                    .queryParam("address_type", addressType)
                .when()
                    .get(getServiceUrl() + "/account-service/account/address-book");
    }

    @Then("^User should get an account address-book response$")
    public void user_should_get_an_account_address_book_response() throws Throwable {
        httpResponse.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("response.results", notNullValue())
                .body("errors", is(Collections.emptyList()));
    }
}

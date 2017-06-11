package cucumber.steps;

import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class getSignOut extends ScenarioBase {
    @When("^User sends Signs out$")
    public void user_sends_Get_SignOut_request() throws Throwable {
        RestAssured
                .given()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/logout")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results.links.home_page_link", notNullValue())
                    .body("errors", is(Collections.emptyList()));
    }

    @When("^User sends Signs out without any cookies$")
    public void userSendsGetSignOutRequestWithoutCookies() throws Throwable {
        RestAssured
                .when()
                    .get(getServiceUrl() + "/account-service/account/logout")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results", notNullValue())
                    .body("errors", is(Collections.emptyList()));
    }
}

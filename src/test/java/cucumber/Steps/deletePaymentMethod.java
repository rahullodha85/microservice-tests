package cucumber.steps;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;

import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;

public class deletePaymentMethod extends ScenarioBase {
    @When("^user sent delete payment-method request$")
    public void deletePayment() throws Throwable {

        Response getResponse = RestAssured
                .given()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/payment");

        ArrayList<Integer> ids = getResponse.body().path("response.results.payment_methods_info.credit_card.id");
        if(ids.size()>0) {
            for (int id : ids) {
                RestAssured.given()
                            .log().all()
                            .header("Cookie", createCookieHeader())
                        .when()
                            .delete(getServiceUrl() + "/account-service/account/payment-action/" + id)
                        .then()
                            .log().ifValidationFails()
                            .assertThat()
                            .statusCode(200)
                            .body("errors", is(Collections.emptyList()));
            }
        }
    }

    @Then("^payment method for that user should be removed$")
    public void verifyDeletePayment() throws Throwable {
        RestAssured
                .given()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/payment")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results.payment_methods_info", is(Collections.emptyList()))
                    .body("errors", is(Collections.emptyList()));
    }
}

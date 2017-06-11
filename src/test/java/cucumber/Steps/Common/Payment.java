package cucumber.steps.common;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.steps.common.ScenarioBase;
import org.hamcrest.CoreMatchers;

import java.util.Collections;
import java.util.List;

public class Payment extends ScenarioBase {

    @After(value={"@Payment"})
    public void cleanup() {
        deleteAllExistingPaymentMethods();
    }

    @Given("^User has no payment methods saved$")
    public void deleteUsersPayments() {
        deleteAllExistingPaymentMethods();
    }

    public void deleteAllExistingPaymentMethods() {

        ValidatableResponse getPaymentsResponse  = RestAssured
                .given()
                    .header("Cookie", createCookieHeader())
                    .log().all()
                .when()
                    .get(getServiceUrl() + "/account-service/account/payment")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("errors", CoreMatchers.is(Collections.emptyList()));

        List<Integer> ids = getPaymentsResponse.extract().body().path("response.results.payment_methods_info.credit_card.id");
        for (int id : ids) {
            RestAssured.given()
                        .log().all()
                        .header("Cookie", createCookieHeader())
                    .when()
                        .delete(getServiceUrl() + "/account-service/account/payment-action/" + id)
                    .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .body("errors", CoreMatchers.is(Collections.emptyList()));
        }
    }

}

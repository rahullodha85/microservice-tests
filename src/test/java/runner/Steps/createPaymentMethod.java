package runner.Steps;

import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import runner.Steps.Common.ScenarioBase;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;

public class createPaymentMethod extends ScenarioBase {

    private Response createPayment(String email, String brand) {
        JsonObject paymentMethod = new JsonObject();
        paymentMethod.addProperty("brand", brand);
        paymentMethod.addProperty("is_default", true);
        paymentMethod.addProperty("month", 4);
        paymentMethod.addProperty("year", 2030);
        paymentMethod.addProperty("name", "Test Name");
        paymentMethod.addProperty("number", "6011000990911111");
        paymentMethod.addProperty("security_code", "");
        paymentMethod.addProperty("user_id", email);
        String jsonBody = paymentMethod.toString();

        return RestAssured
                .given()
                    .header("Cookie", createCookieHeader())
                    .header("Content-Type", "application/json")
                    .log().all()
                    .body(jsonBody)
                .when()
                    .post(getServiceUrl() + "/account-service/account/payment-action");
    }

    @When("^user (.+) sent valid create payment-method request$")
    public void createValidPayment(String email) throws Throwable {
        httpResponse = createPayment(email, "DISC");
    }

    @Then("^user should get that payment-method in get payment request$")
    public void verifyPayment() throws Throwable {
        RestAssured
                .given()
                .header("Cookie", createCookieHeader())
                .when().get(getServiceUrl() + "/account-service/account/payment")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("response.results.payment_methods_info[0].credit_card.brand", is("DISC"))
                .body("response.results.payment_methods_info[0].credit_card.is_default", is(true))
                .body("response.results.payment_methods_info[0].credit_card.month", is(4))
                .body("response.results.payment_methods_info[0].credit_card.year", is(2030))
                .body("response.results.payment_methods_info[0].credit_card.name", is("Test Name"))
                .body("response.results.payment_methods_info[0].credit_card.number", is("****1111"))
                .body("response.results.payment_methods_info[0].credit_card.security_code", is(""))
                .body("response.results.payment_methods_info.size()", is(1))
                .body("errors", is(Collections.emptyList()));
    }


    @When("^user (.+) sent invalid create payment-method request$")
    public void createInvalidPaymentBecauseTheNumberIsNotAVisaCardNumber(String email) throws Throwable {
        httpResponse = createPayment(email, "VISA");
    }

    @When("^user (.+) send create payment-method request with same credit card$")
    public void createDuplicatePayment(String email) throws Throwable {
        httpResponse = createPayment(email, "DISC");
    }
}

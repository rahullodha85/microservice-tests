package runner.Steps;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import runner.Steps.Common.ScenarioBase;
import runner.util.JsonFileReader;
import org.hamcrest.CoreMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class defaultPaymentMethod extends ScenarioBase {

    @When("^User with (.+) creates two payment methods$")
    public void createTwoPaymentMethods(String userId) throws Exception {
        List<JsonObject> paymentMethods = new ArrayList<>();

        JsonObject paymentMethod1 = JsonFileReader.ReadFileToJsonObject("requests/createPaymentMethod.json");
        paymentMethod1.addProperty("user_id", userId);
        paymentMethod1.addProperty("number", "6011000990911111");
        paymentMethod1.addProperty("is_default", true);
        paymentMethods.add(paymentMethod1);

        JsonObject paymentMethod2 = JsonFileReader.ReadFileToJsonObject("requests/createPaymentMethod.json");
        paymentMethod2.addProperty("user_id", userId);
        paymentMethod2.addProperty("number", "6011000990911131");
        paymentMethod2.addProperty("is_default", false);
        paymentMethods.add(paymentMethod2);

        for (JsonObject paymentMethod : paymentMethods) {
            RestAssured
                    .given()
                        .log().all()
                        .header("Cookie", createCookieHeader())
                        .header("Content-Type", "application/json")
                        .body(paymentMethod.toString())
                    .when()
                        .post(getServiceUrl() + "/account-service/account/payment-action")
                    .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .body("errors", CoreMatchers.is(Collections.emptyList()));
        }
    }

    @Then("^After deleting the default payment method the other payment will be marked as default$")
    public void deleteThenCheckForDefault() {

        ValidatableResponse getPaymentMethodsResponse = RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/payment")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("errors", CoreMatchers.is(Collections.emptyList()));

        JsonArray paymentMethodsFromResponse = getPaymentMethodsFromResponse(getPaymentMethodsResponse);
        for (JsonElement paymentMethod : paymentMethodsFromResponse) {
            JsonObject credit_card = paymentMethod.getAsJsonObject().get("credit_card").getAsJsonObject();
            if (credit_card.get("is_default").getAsBoolean()) {
                String id = credit_card.get("id").getAsString();

                RestAssured
                        .given()
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

        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/payment")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results.payment_methods_info[0].credit_card.is_default", is(true))
                    .body("errors", CoreMatchers.is(Collections.emptyList()));
    }

    public JsonArray getPaymentMethodsFromResponse(ValidatableResponse getResponse) {
        String responseBody = getResponse.extract().body().asString();
        JsonElement paymentMethodsResponse = new JsonParser().parse(responseBody);
        return paymentMethodsResponse.getAsJsonObject().get("response").getAsJsonObject().get("results").getAsJsonObject().get("payment_methods_info").getAsJsonArray();
    }
}

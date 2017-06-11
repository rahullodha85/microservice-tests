package cucumber.steps;

import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;
import org.hamcrest.CoreMatchers;

import java.util.Collections;

public class orderStatus extends ScenarioBase {
    @When("^User checks order status with zip (.+)$")
    public void user_checks_order_status_for_and(String zip) throws Throwable {
        httpResponse = RestAssured
                .given()
                    .log().all()
                    .header("content-type", "application/json")
                    .body(getOrderStatusPostBody(zip))
                .when()
                    .post(getServiceUrl() + "/account-service/order-status-action");
    }

    @When("^Check order status with zip (.+) with session (.+)$")
    public void OrderStatusWithBadSession(String zip, String sessionId) throws Throwable {
        httpResponse = RestAssured
                .given()
                    .log().all()
                    .header("content-type", "application/json")
                    .header("Cookie", "JSESSIONID=" + sessionId)
                    .body(getOrderStatusPostBody(zip))
                .when()
                    .post(getServiceUrl() + "/account-service/order-status-action");
    }

    @Then("^User should get order status$")
    public void user_should_get_status_of_that() throws Throwable {
        httpResponse.then()
                .log().ifValidationFails()
                .assertThat()
                .body("response.results", CoreMatchers.not(Collections.emptyList()))
                .body("response.results.order.order_num", CoreMatchers.is(getOrderNumber()));
    }

    private String getOrderStatusPostBody(String zip) {
        JsonObject root = new JsonObject();
        root.addProperty("order_num", getOrderNumber());
        root.addProperty("billing_zip_code", zip);
        return root.toString();
    }

    private String getOrderNumber() {
        return config.getString("orderNumber");
    }
}

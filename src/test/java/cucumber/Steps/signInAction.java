package cucumber.steps;

import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;

/**
 * Created by 461967 on 5/13/2016.
 */
public class signInAction extends ScenarioBase {
    @When("^User signs in with (.+) and (.+) with (.+)$")
    public void user_signs_in_with_abc_email_test_and_test_with_test(String email, String pwd, String sessionId) throws Throwable {
        httpResponse = RestAssured
                .given()
                    .log().all()
                    .header("content-type", "application/json")
                    .header("Cookie", "JSESSIONID=" + sessionId)
                    .body(getLoginBody(email, pwd))
                .when()
                    .post(getServiceUrl() + "/account-service/sign-in-action");
    }

    @Then("^User should be successfully signed in$")
    public void user_should_be_successfully_signed_in() throws Throwable {
        httpResponse.then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200);
    }

    private static String getLoginBody(String email, String pwd) {
        JsonObject root = new JsonObject();
        root.addProperty("username", email);
        root.addProperty("password", pwd);
        return root.toString();
    }
}

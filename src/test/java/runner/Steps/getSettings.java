package runner.Steps;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import runner.Steps.Common.ScenarioBase;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasItems;

public class getSettings extends ScenarioBase {

    @When("^User checks account settings$")
    public void user_checks_account_settings() throws Throwable {
        httpResponse = RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/settings");
    }

    @Then("^User should get an account settings response$")
    public void user_should_get_an_account_settings_response() throws Throwable {
        httpResponse.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("response.results", notNullValue())
                .body("errors", is(Collections.emptyList()));
    }

    @When("^User (.+) removes all email preferences$")
    public void userRemovesEmailPreferences(String email) throws Throwable {
        JsonObject preferences = new JsonObject();
        preferences.add("preferences", new JsonArray());
        preferences.addProperty("user_id", email);
        preferences.toString();

        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("content-type", "application/json")
                    .body(preferences.toString())
                .when()
                    .put(getServiceUrl() + "/account-service/account/settings/email-preferences")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results.preferences", is(Collections.emptyList()))
                    .body("errors", is(Collections.emptyList()));
    }

    @Then("^User should see no email preferences$")
    public void verifyNoEmailPreferences() throws Throwable {
        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/settings")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results.email_preferences.preferences", is(Collections.emptyList()))
                    .body("errors", is(Collections.emptyList()));
    }

    @When("^User (.+) selects all email preferences$")
    public void userAddsEmailPreferences(String email) throws Throwable {
        JsonArray emailPreferences = new JsonArray();
        emailPreferences.add(new JsonPrimitive("off5th_opt_status"));
        emailPreferences.add(new JsonPrimitive("saks_opt_status"));
        emailPreferences.add(new JsonPrimitive("off5th_canada_opt_status"));
        emailPreferences.add(new JsonPrimitive("saks_canada_opt_status"));

        JsonObject preferences = new JsonObject();
        preferences.add("preferences", emailPreferences);
        preferences.addProperty("user_id", email);
        preferences.toString();

        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("content-type", "application/json")
                    .body(preferences.toString())
                .when()
                    .put(getServiceUrl() + "/account-service/account/settings/email-preferences")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results.preferences",
                        hasItems("off5th_opt_status",
                                "saks_opt_status",
                                "off5th_canada_opt_status",
                                "saks_canada_opt_status"))
                    .body("errors", is(Collections.emptyList()));
    }

    @When("^User (.+) with bad JSessionId updates email preferences$")
    public void userUpdatesEmailPreferences(String email) throws Throwable {
        cookies.add("UserName=" + email);
        cookies.add(String.format("JSESSIONID=%s", "invalidSessionId"));

        JsonArray emailPreferences = new JsonArray();
        emailPreferences.add(new JsonPrimitive("off5th_opt_status"));
        emailPreferences.add(new JsonPrimitive("saks_opt_status"));
        emailPreferences.add(new JsonPrimitive("off5th_canada_opt_status"));
        emailPreferences.add(new JsonPrimitive("saks_canada_opt_status"));

        JsonObject preferences = new JsonObject();
        preferences.add("preferences", emailPreferences);
        preferences.addProperty("user_id", email);
        preferences.toString();

        httpResponse = RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("content-type", "application/json")
                    .body(preferences.toString())
                .when()
                    .put(getServiceUrl() + "/account-service/account/settings/email-preferences");
    }


    @Then("^User should see all email preferences$")
    public void verifyAllEmailPreferences() throws Throwable {
        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/settings")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results.email_preferences.preferences",
                            hasItems("off5th_opt_status",
                            "saks_opt_status",
                            "off5th_canada_opt_status",
                            "saks_canada_opt_status"))
                    .body("errors", is(Collections.emptyList()));
    }
}

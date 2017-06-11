package cucumber.steps;

import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;
import cucumber.util.JsonFileReader;

import java.util.Collections;

import static cucumber.steps.common.CommonActions.getLoginPostBody;
import static org.hamcrest.CoreMatchers.is;

public class createAccount extends ScenarioBase {

    @When("^User creates an account$")
    public void createNewAccount() throws Exception {

        String userId = String.valueOf(java.util.UUID.randomUUID()).substring(0, 8) + "@email.test";
        System.out.println("USERID: " + userId);
        String password = "test123";

        JsonObject account = JsonFileReader.ReadFileToJsonObject("requests/createAccount.json");
        account.addProperty("email", userId);
        account.addProperty("password", password);
        account.addProperty("confirm_password", password);

        context.put("email", userId);
        context.put("password", password);

        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("Content-Type", "application/json")
                    .body(account.toString())
                .when()
                    .post(getServiceUrl() + "/account-service/register-action")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("errors", is(Collections.emptyList()));
    }

    @Then("^user should be able to login to make sure account was properly (.+)")
    public void verifyLoginForNewAccount(String notImportant) {
        String userId = (String) context.get("email");
        String password = (String) context.get("password");

        ValidatableResponse validatableResponse = RestAssured
                .given()
                    .log().all()
                    .header("content-type", "application/json")
                    .body(getLoginPostBody(userId, password))
                .when()
                    .post(getServiceUrl() + "/account-service/sign-in-action")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200);

        String jsessionid = validatableResponse.extract().cookie("JSESSIONID");
        cookies.add(String.format("JSESSIONID=%s", jsessionid));
        cookies.add("UserName=" + userId);
    }

    @And("^user can change password$")
    public void changePassword() {

        String userId = (String) context.get("email");
        String password = (String) context.get("password");

        JsonObject changePasswordRequest = new JsonObject();
        changePasswordRequest.addProperty("user_id", userId);
        changePasswordRequest.addProperty("old_password", password);
        changePasswordRequest.addProperty("new_password", "new123");
        changePasswordRequest.addProperty("confirm_password", "new123");

        context.put("password", "new123");

        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("Content-Type", "application/json")
                    .body(changePasswordRequest.toString())
                .when()
                    .post(getServiceUrl() + "/account-service/account/change-password-action")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("errors", is(Collections.emptyList()));
    }

    @When("^user updates their email address$")
    public void userUpdatesTheirEmailAddress() {
        String newEmailAddress = String.valueOf(java.util.UUID.randomUUID()).substring(0, 8) + "@email.test";
        context.put("email", newEmailAddress);

        JsonObject updateAccountRequest = new JsonObject();
        updateAccountRequest.addProperty("first_name", randomName(5));
        updateAccountRequest.addProperty("last_name", randomName(5));
        updateAccountRequest.addProperty("email", newEmailAddress);

        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("Content-Type", "application/json")
                    .body(updateAccountRequest.toString())
                .when()
                    .post(getServiceUrl() + "/account-service/account/profile-action")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("errors", is(Collections.emptyList()));
    }
}

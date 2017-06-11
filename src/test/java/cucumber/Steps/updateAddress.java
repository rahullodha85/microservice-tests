package cucumber.steps;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;
import cucumber.util.JsonFileReader;
import org.hamcrest.CoreMatchers;

import java.util.Collections;

import static org.hamcrest.core.Is.is;

public class updateAddress extends ScenarioBase {
    @Given("^User (.+) creates address in address-book with address-type (.+)$")
    public void userCreatesAddress(String email, String addressType) throws Throwable {
        String postBody = createAddressBody(email, addressType);

        ValidatableResponse getResponse = RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("Content-Type", "application/json")
                    .body(postBody)
                .when()
                    .post(getServiceUrl() + "/account-service/account/address")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("errors", CoreMatchers.is(Collections.emptyList()));

        String responseBody = getResponse.extract().body().asString();
        JsonElement responseBodyJson = new JsonParser().parse(responseBody);
        context.put("addressResponse", responseBodyJson);
    }

    @When("User with (.+) changes (.+) address to have street address (.+)$")
    public void userChangesAddress(String userId, String addressType, String addressLine1) throws Throwable {
        updateAddressThenValidateThatFieldWasProperlyUpdated(userId, addressType, addressLine1);
    }


    @Then("^User with (.+) should be able to change (.+) address back to have street address (.+)$")
    public void userChangesAddressBack(String userId, String addressType, String addressLine1) {
        updateAddressThenValidateThatFieldWasProperlyUpdated(userId, addressType, addressLine1);
    }

    private String createAddressBody(String email, String addressType) throws Exception {
        JsonObject address = JsonFileReader.ReadFileToJsonObject("requests/createAddress.json");

        address.addProperty("user_id", email);
        address.addProperty("address_type", addressType);

        return address.toString();
    }

    private void updateAddressThenValidateThatFieldWasProperlyUpdated(String userId, String addressType, String addressLine1) {
        JsonElement addressResponse = (JsonElement) context.get("addressResponse");
        JsonObject address = extractFirstAddress(addressResponse);
        String addressId = address.get("id").getAsString();

        address.addProperty("address1", addressLine1);
        address.addProperty("user_id", userId);
        address.addProperty("address_type", addressType);
        address.remove("id");

        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("Content-Type", "application/json")
                .body(address.toString())
                .when()
                    .put(getServiceUrl() + "/account-service/account/address/" + addressId)
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("errors", CoreMatchers.is(Collections.emptyList()));

        ValidatableResponse getResponse = RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .queryParam("address_type", addressType)
                .when()
                    .get(getServiceUrl() + "/account-service/account/address-book")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results.addresses[0].address1", is(addressLine1))
                    .body("errors", CoreMatchers.is(Collections.emptyList()));

        String responseBody = getResponse.extract().body().asString();
        JsonElement responseBodyJson = new JsonParser().parse(responseBody);
        context.put("addressResponse", responseBodyJson);
    }

    private JsonObject extractFirstAddress(JsonElement addressResponse) {
        return addressResponse.getAsJsonObject().get("response").getAsJsonObject().get("results").getAsJsonObject().get("addresses").getAsJsonArray().get(0).getAsJsonObject();
    }
}

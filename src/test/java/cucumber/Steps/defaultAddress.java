package cucumber.steps;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;
import cucumber.util.JsonFileReader;
import org.hamcrest.CoreMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class defaultAddress extends ScenarioBase {

    @When("^User with (.+) creates (.+) address and marks it as default (.+)$")
    public void createAddress(String userId, String addressType, boolean isDefault) throws Throwable {

        JsonObject address = JsonFileReader.ReadFileToJsonObject("requests/createAddress.json");
        address.addProperty("user_id", userId);
        address.addProperty("address_type", addressType);
        address.addProperty("is_default", isDefault);

        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("Content-Type", "application/json")
                    .body(address.toString())
                .when()
                    .post(getServiceUrl() + "/account-service/account/address")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("errors", CoreMatchers.is(Collections.emptyList()));
    }

    @Then("^User will see (.+) address as default anyway because it is the only address$")
    public void getAddressAndCheckItIsDefault(String addressType) {

        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .queryParam("address_type", addressType)
                .when()
                    .get(getServiceUrl() + "/account-service/account/address-book")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results.addresses[0].is_default", is(true))
                    .body("errors", CoreMatchers.is(Collections.emptyList()));
    }

    @When("^User with (.+) creates two (.+) addresses$")
    public void createTwoAddresses(String userId, String addressType) throws Exception {
        List<JsonObject> addresses = new ArrayList<>();

        JsonObject address1 = JsonFileReader.ReadFileToJsonObject("requests/createAddress.json");
        address1.addProperty("user_id", userId);
        address1.addProperty("address_type", addressType);
        address1.addProperty("is_default", true);
        address1.addProperty("address1", "abc 1 st");
        addresses.add(address1);

        JsonObject address2 = JsonFileReader.ReadFileToJsonObject("requests/createAddress.json");
        address2.addProperty("user_id", userId);
        address2.addProperty("address_type", addressType);
        address2.addProperty("is_default", false);
        address2.addProperty("address1", "abc 2 st");
        addresses.add(address2);

        for (JsonObject address : addresses) {
            RestAssured
                    .given()
                        .log().all()
                        .header("Cookie", createCookieHeader())
                        .header("Content-Type", "application/json")
                        .body(address.toString())
                    .when()
                        .post(getServiceUrl() + "/account-service/account/address")
                    .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .body("errors", CoreMatchers.is(Collections.emptyList()));
        }
    }

    @Then("^After deleting the default (.+) address the other address will be marked as default$")
    public void deleteThenCheckForDefault(String addressType) {

        ValidatableResponse getAddressResponse = RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .queryParam("address_type", addressType)
                .when()
                    .get(getServiceUrl() + "/account-service/account/address-book")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("errors", CoreMatchers.is(Collections.emptyList()));

        JsonArray addresses = getAddressesFromResponse(getAddressResponse);
        for (JsonElement address : addresses) {
            if (address.getAsJsonObject().get("is_default").getAsBoolean()) {
                String addressId = address.getAsJsonObject().get("id").getAsString();

                RestAssured
                        .given()
                        .log().all()
                        .header("Cookie", createCookieHeader())
                        .queryParam("address_type", addressType)
                        .when()
                            .delete(getServiceUrl() + "/account-service/account/address/" + addressId)
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
                    .queryParam("address_type", addressType)
                .when()
                    .get(getServiceUrl() + "/account-service/account/address-book")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results.addresses[0].is_default", is(true))
                    .body("errors", CoreMatchers.is(Collections.emptyList()));
    }


    public JsonArray getAddressesFromResponse(ValidatableResponse getResponse) {
        String responseBody = getResponse.extract().body().asString();
        JsonElement addressResponse = new JsonParser().parse(responseBody);
        return addressResponse.getAsJsonObject().get("response").getAsJsonObject().get("results").getAsJsonObject().get("addresses").getAsJsonArray();
    }
}

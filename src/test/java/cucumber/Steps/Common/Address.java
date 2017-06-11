package cucumber.steps.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import org.hamcrest.CoreMatchers;

import java.util.Collections;

public class Address extends ScenarioBase {


    @After(value={"@Address"})
    public void cleanup() {
        deleteAllExistingAddresses("shipping");
        deleteAllExistingAddresses("billing");
    }

    @Given("^User has no (.+) addresses saved$")
    public void userHasNoAddresses(String addressType) throws Throwable {
        deleteAllExistingAddresses(addressType);
    }

    public void deleteAllExistingAddresses(String addressType) {

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

    public JsonArray getAddressesFromResponse(ValidatableResponse getResponse) {
        String responseBody = getResponse.extract().body().asString();
        JsonElement addressResponse = new JsonParser().parse(responseBody);
        return addressResponse.getAsJsonObject().get("response").getAsJsonObject().get("results").getAsJsonObject().get("addresses").getAsJsonArray();
    }
}

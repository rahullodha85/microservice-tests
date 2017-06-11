package cucumber.steps;

import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.steps.common.ScenarioBase;
import cucumber.util.JsonFileReader;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;

public class createAddress extends ScenarioBase {

    @When("^User with (.+) creates the same (.+) address twice$")
    public void createTwoSameAddresses(String userId, String addressType) throws Exception {

        JsonObject address = JsonFileReader.ReadFileToJsonObject("requests/createAddress.json");
        address.addProperty("user_id", userId);
        address.addProperty("address_type", addressType);

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
                    .body("errors", is(Collections.emptyList()));

        httpResponse = RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("Content-Type", "application/json")
                    .body(address.toString())
                .when()
                    .post(getServiceUrl() + "/account-service/account/address");
    }

    @When("^User with (.+) creates a (.+) address with an invalid canadian postal code$")
    public void createAddressWithInvalidCanadianPostalCode(String userId, String addressType) throws Exception {
        createCanadianAddressWithProviceAndPostalCode("AB", "V2H 0K8", userId, addressType);
    }

    @When("^User with (.+) creates a (.+) address with province (.+) and a valid canadian postal code (.+)$")
    public void createAddressWithValidCanadianPostalCode(String userId, String addressType, String province, String postalCode) throws Exception {
        createCanadianAddressWithProviceAndPostalCode(province, postalCode, userId, addressType);
    }

    private void createCanadianAddressWithProviceAndPostalCode(String province, String postalCode, String userId, String addressType) throws Exception {

        JsonObject address = JsonFileReader.ReadFileToJsonObject("requests/createAddress.json");
        address.addProperty("user_id", userId);
        address.addProperty("address_type", addressType);
        address.addProperty("country", "CA");
        address.addProperty("state", province);
        address.addProperty("zip", postalCode);

        httpResponse = RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("Content-Type", "application/json")
                    .body(address.toString())
                .when()
                    .post(getServiceUrl() + "/account-service/account/address");
    }

    @Then("^User will successfully save that (.+) address and see the province (.+) and postal code (.+)$")
    public void successfullySaveAddress(String addressType, String province, String postalCode) {
        httpResponse
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("errors", is(Collections.emptyList()));

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
                    .body("response.results.addresses[0].zip", is(postalCode))
                    .body("response.results.addresses[0].state", is(province))
                    .body("errors", is(Collections.emptyList()));
    }

    @When("^User with (.+) successfully creates an international (.+) address with a province longer than 3 characters$")
    public void createInternationalAddress(String userId, String addressType) throws Exception {
        JsonObject address = JsonFileReader.ReadFileToJsonObject("requests/createAddress.json");
        address.addProperty("user_id", userId);
        address.addProperty("address_type", addressType);
        address.addProperty("country", "AU");
        address.addProperty("state", "Queensland");
        address.addProperty("zip", "5024");

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
                    .body("errors", is(Collections.emptyList()));
    }

    @Then("^User should be able to successfully retrieve that (.+) address$")
    public void retrieveInternationalAddress(String addressType) {
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
                .body("response.results.addresses[0].state", is("Queensland"))
                .body("errors", is(Collections.emptyList()));
    }
}

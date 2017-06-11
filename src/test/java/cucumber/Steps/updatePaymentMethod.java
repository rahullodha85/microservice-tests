package cucumber.steps;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.steps.common.ScenarioBase;
import cucumber.util.JsonFileReader;
import org.hamcrest.CoreMatchers;
import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;

public class updatePaymentMethod extends ScenarioBase {
    private static LocalDate localDate = new LocalDate();
    private static int year = localDate.getYear() + 1;

    @And("^User (.+) creates a chinese union pay card$")
    public void updatePaymentName(String email) throws Throwable {
        JsonObject creditCard = createChineseUnionPayCreditCardRequestBody(email);

        ValidatableResponse createCardResponse = RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("Content-Type", "application/json")
                    .body(creditCard.toString())
                .when()
                    .post(getServiceUrl() + "/account-service/account/payment-action")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("errors", CoreMatchers.is(Collections.emptyList()));

        context.put("paymentMethodId", getPaymentMethodId(createCardResponse));
    }

    private JsonObject createChineseUnionPayCreditCardRequestBody(String email) throws Exception {
        JsonObject creditCard = JsonFileReader.ReadFileToJsonObject("requests/createPaymentMethod.json");
        creditCard.addProperty("brand", "CUP");
        creditCard.addProperty("number", "6221260004598750");
        creditCard.addProperty("name", "some name");
        creditCard.addProperty("user_id", email);
        return creditCard;
    }

    @Then("^When user (.+) updates the name of the card, it is still a chinese union pay card$")
    public void getUpdatedPayment(String email) throws Throwable {
        String updatedName = randomName(10) + " " + randomName(10);
        int month = new Random().nextInt(11) + 1;
        JsonObject update = new JsonObject();
        update.addProperty("id", (int) context.get("paymentMethodId"));
        update.addProperty("name", updatedName);
        update.addProperty("month", month);
        update.addProperty("year", year);
        update.addProperty("brand", "CUP");
        update.addProperty("user_id", email);
        update.addProperty("is_default", true);

        RestAssured
                .given()
                    .log().all()
                    .header("Cookie", createCookieHeader())
                    .header("Content-Type", "application/json")
                    .body(update.toString())
                .when()
                    .put(getServiceUrl() + "/account-service/account/payment-action")
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("response.results.payment_methods_info[0].credit_card.brand", is("CUP"))
                    .body("response.results.payment_methods_info[0].display_brand_name", is("China UnionPay Credit"))
                    .body("errors", CoreMatchers.is(Collections.emptyList()));

        RestAssured
                .given()
                    .header("Cookie", createCookieHeader())
                .when()
                    .get(getServiceUrl() + "/account-service/account/payment")
                .then()
                    .log().ifValidationFails()
                    .assertThat()
                    .statusCode(200)
                    .body("response.results.payment_methods_info[0].credit_card.name", is(updatedName))
                    .body("response.results.payment_methods_info[0].credit_card.brand", is("CUP"))
                    .body("response.results.payment_methods_info[0].display_brand_name", is("China UnionPay Credit"))
                    .body("response.results.payment_methods_info[0].credit_card.month", is(month))
                    .body("response.results.payment_methods_info[0].credit_card.year", is(year))
                    .body("errors", is(Collections.emptyList()));
    }

    private int getPaymentMethodId(ValidatableResponse validatableResponse) {
        JsonObject jsonResponse = new JsonParser().parse(validatableResponse.extract().body().asString()).getAsJsonObject();
        JsonObject paymentMethod = jsonResponse.get("response").getAsJsonObject().get("results").getAsJsonObject().get("payment_methods_info").getAsJsonArray().get(0).getAsJsonObject();
        return paymentMethod.get("credit_card").getAsJsonObject().get("id").getAsInt();
    }
}

package com.atm.api;

import com.atm.utils.ApiClient;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class CardStepDefs {

    private Response response;
    private String lastIssuedCardNumber;

    @When("I issue a {string} card for account {string}")
    public void iIssueACardForAccount(String cardType, String accountNumber) {
        response = ApiClient.issueCard(accountNumber, cardType);
        if (response.statusCode() == 201) {
            lastIssuedCardNumber = response.jsonPath().getString("cardNumber");
        }
    }

    @When("I block the last issued card")
    public void iBlockTheLastIssuedCard() {
        assertThat(lastIssuedCardNumber)
                .as("No card has been issued yet in this scenario")
                .isNotNull();
        response = ApiClient.blockCard(lastIssuedCardNumber);
    }

    @When("I unblock the last issued card")
    public void iUnblockTheLastIssuedCard() {
        assertThat(lastIssuedCardNumber)
                .as("No card has been issued yet in this scenario")
                .isNotNull();
        response = ApiClient.unblockCard(lastIssuedCardNumber);
    }

    @When("I validate PIN {string} for the last issued card")
    public void iValidatePinForTheLastIssuedCard(String pin) {
        assertThat(lastIssuedCardNumber)
                .as("No card has been issued yet in this scenario")
                .isNotNull();
        response = ApiClient.validatePin(lastIssuedCardNumber, pin);
    }

    // Keep old steps for backward compatibility
    @When("I block card {string}")
    public void iBlockCard(String cardNumber) {
        response = ApiClient.blockCard(cardNumber);
    }

    @When("I unblock card {string}")
    public void iUnblockCard(String cardNumber) {
        response = ApiClient.unblockCard(cardNumber);
    }

    @When("I validate PIN {string} for card {string}")
    public void iValidatePin(String pin, String cardNumber) {
        response = ApiClient.validatePin(cardNumber, pin);
    }

    @Then("the card should be issued successfully")
    public void theCardShouldBeIssuedSuccessfully() {
        response.then()
                .statusCode(201)
                .body("cardNumber", notNullValue())
                .body("status", equalTo("ACTIVE"))
                .body("contactless", equalTo(true));
    }

    @Then("the card status should be {string}")
    public void theCardStatusShouldBe(String status) {
        response.then().body("status", equalTo(status));
    }

    @Then("the PIN validation result should be {string}")
    public void thePinValidationResultShouldBe(String expected) {
        boolean expectedBool = Boolean.parseBoolean(expected);
        response.then().body("valid", equalTo(expectedBool));
    }

    @Then("the issued card number should not be null")
    public void theIssuedCardNumberShouldNotBeNull() {
        assertThat(lastIssuedCardNumber).isNotNull().isNotEmpty();
    }
}
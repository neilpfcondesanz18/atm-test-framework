package com.atm.api;

import com.atm.utils.ApiClient;
import com.atm.utils.ScenarioContext;
import io.cucumber.java.en.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class CardStepDefs {

    private final ScenarioContext ctx;

    public CardStepDefs(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @When("I issue a {string} card for the created account")
    public void iIssueACardForTheCreatedAccount(String cardType) {
        assertThat(ctx.currentAccountNumber).isNotNull();
        ctx.lastResponse = ApiClient.issueCard(ctx.currentAccountNumber, cardType);
        if (ctx.lastResponse.statusCode() == 201) {
            ctx.lastIssuedCardNumber = ctx.lastResponse.jsonPath().getString("cardNumber");
        }
    }

    @When("I issue a {string} card for account {string}")
    public void iIssueACardForAccount(String cardType, String accountNumber) {
        ctx.lastResponse = ApiClient.issueCard(accountNumber, cardType);
        if (ctx.lastResponse.statusCode() == 201) {
            ctx.lastIssuedCardNumber = ctx.lastResponse.jsonPath().getString("cardNumber");
        }
    }

    @When("I block the last issued card")
    public void iBlockTheLastIssuedCard() {
        assertThat(ctx.lastIssuedCardNumber).isNotNull();
        ctx.lastResponse = ApiClient.blockCard(ctx.lastIssuedCardNumber);
    }

    @When("I unblock the last issued card")
    public void iUnblockTheLastIssuedCard() {
        assertThat(ctx.lastIssuedCardNumber).isNotNull();
        ctx.lastResponse = ApiClient.unblockCard(ctx.lastIssuedCardNumber);
    }

    @When("I validate PIN {string} for the last issued card")
    public void iValidatePinForTheLastIssuedCard(String pin) {
        assertThat(ctx.lastIssuedCardNumber).isNotNull();
        ctx.lastResponse = ApiClient.validatePin(ctx.lastIssuedCardNumber, pin);
    }

    @When("I validate PIN {string} for card {string}")
    public void iValidatePin(String pin, String cardNumber) {
        ctx.lastResponse = ApiClient.validatePin(cardNumber, pin);
    }

    @Then("the card should be issued successfully")
    public void theCardShouldBeIssuedSuccessfully() {
        ctx.lastResponse.then()
                .statusCode(201)
                .body("cardNumber", notNullValue())
                .body("status", equalTo("ACTIVE"))
                .body("contactless", equalTo(true));
    }

    @Then("the card status should be {string}")
    public void theCardStatusShouldBe(String status) {
        ctx.lastResponse.then().body("status", equalTo(status));
    }

    @Then("the PIN validation result should be {string}")
    public void thePinValidationResultShouldBe(String expected) {
        boolean expectedBool = Boolean.parseBoolean(expected);
        ctx.lastResponse.then().body("valid", equalTo(expectedBool));
    }

    @Then("the issued card number should not be null")
    public void theIssuedCardNumberShouldNotBeNull() {
        assertThat(ctx.lastIssuedCardNumber).isNotNull().isNotEmpty();
    }
}

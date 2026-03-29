package com.atm.api;

import com.atm.utils.ApiClient;
import com.atm.utils.TestDataFactory;
import io.cucumber.java.en.*;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class AccountStepDefs {

    private Response response;
    private Map<String, Object> accountPayload = new HashMap<>();
    private String currentAccountNumber;

    @Given("the banking API is available")
    @Step("Verify banking API health")
    public void theBankingApiIsAvailable() {
        ApiClient.healthCheck()
                .then().statusCode(200)
                .body("status", equalTo("UP"));
    }

    @Given("I have a valid account payload")
    public void iHaveAValidAccountPayload() {
        accountPayload = TestDataFactory.validAccount();
        currentAccountNumber = (String) accountPayload.get("accountNumber");
    }

    @Given("I have an account payload with balance {double}")
    public void iHaveAnAccountPayloadWithBalance(double balance) {
        accountPayload = TestDataFactory.accountWithBalance(balance);
        currentAccountNumber = (String) accountPayload.get("accountNumber");
    }

    @When("I create the account")
    @Step("POST /api/v1/accounts")
    public void iCreateTheAccount() {
        response = ApiClient.createAccount(accountPayload);
    }

    @When("I retrieve account {string}")
    public void iRetrieveAccount(String accountNumber) {
        response = ApiClient.getAccount(accountNumber);
    }

    @When("I withdraw {double} from account {string} at ATM {string}")
    @Step("Withdraw {amount} from {accountNumber}")
    public void iWithdraw(double amount, String accountNumber, String atmId) {
        response = ApiClient.withdraw(accountNumber, amount, atmId);
    }

    @When("I deposit {double} into account {string}")
    public void iDeposit(double amount, String accountNumber) {
        response = ApiClient.deposit(accountNumber, amount, "ATM");
    }

    @When("I transfer {double} from {string} to {string}")
    public void iTransfer(double amount, String from, String to) {
        response = ApiClient.transfer(from, to, amount);
    }

    @When("I freeze account {string}")
    public void iFreezeAccount(String accountNumber) {
        response = ApiClient.freezeAccount(accountNumber);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        response.then().statusCode(status);
    }

    @Then("the account should be created successfully")
    public void theAccountShouldBeCreatedSuccessfully() {
        response.then()
                .statusCode(201)
                .body("accountNumber", equalTo(currentAccountNumber))
                .body("status", equalTo("ACTIVE"))
                .body("balance", notNullValue());
    }

    @Then("the balance after should be {double}")
    public void theBalanceAfterShouldBe(double expected) {
        response.then().body("balanceAfter", equalTo((float) expected));
    }

    @Then("the transaction type should be {string}")
    public void theTransactionTypeShouldBe(String type) {
        response.then().body("type", equalTo(type));
    }

    @Then("the error message should contain {string}")
    public void theErrorMessageShouldContain(String text) {
        String error = response.jsonPath().getString("error");
        Assertions.assertThat(error).containsIgnoringCase(text);
    }

    @Then("the account status should be {string}")
    public void theAccountStatusShouldBe(String status) {
        response.then().body("status", equalTo(status));
    }
}

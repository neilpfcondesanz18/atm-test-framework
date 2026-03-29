package com.atm.api;

import com.atm.utils.ApiClient;
import com.atm.utils.ScenarioContext;
import com.atm.utils.TestDataFactory;
import io.cucumber.java.en.*;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class AccountStepDefs {

    private final ScenarioContext ctx;
    private Map<String, Object> accountPayload = new HashMap<>();

    // Cucumber injects ScenarioContext via PicoContainer
    public AccountStepDefs(ScenarioContext ctx) {
        this.ctx = ctx;
    }

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
    }

    @Given("I have an account payload with balance {double}")
    public void iHaveAnAccountPayloadWithBalance(double balance) {
        accountPayload = TestDataFactory.accountWithBalance(balance);
    }

    @When("I create the account")
    @Step("POST /api/v1/accounts")
    public void iCreateTheAccount() {
        ctx.lastResponse = ApiClient.createAccount(accountPayload);
        if (ctx.lastResponse.statusCode() == 201) {
            ctx.currentAccountNumber = ctx.lastResponse.jsonPath().getString("accountNumber");
        }
    }

    @When("I create a second account with balance {double}")
    public void iCreateASecondAccountWithBalance(double balance) {
        Map<String, Object> payload = TestDataFactory.accountWithBalance(balance);
        Response r = ApiClient.createAccount(payload);
        ctx.secondAccountNumber = r.jsonPath().getString("accountNumber");
    }

    @When("I retrieve account {string}")
    public void iRetrieveAccount(String accountNumber) {
        ctx.lastResponse = ApiClient.getAccount(accountNumber);
    }

    @When("I withdraw {double} from the created account at ATM {string}")
    @Step("Withdraw {amount} from created account at {atmId}")
    public void iWithdrawFromCreatedAccount(double amount, String atmId) {
        ctx.lastResponse = ApiClient.withdraw(ctx.currentAccountNumber, amount, atmId);
    }

    @When("I withdraw {double} from account {string} at ATM {string}")
    public void iWithdrawFromAccount(double amount, String accountNumber, String atmId) {
        ctx.lastResponse = ApiClient.withdraw(accountNumber, amount, atmId);
    }

    @When("I deposit {double} into the created account")
    public void iDepositIntoCreatedAccount(double amount) {
        ctx.lastResponse = ApiClient.deposit(ctx.currentAccountNumber, amount, "ATM");
    }

    @When("I deposit {double} into account {string}")
    public void iDeposit(double amount, String accountNumber) {
        ctx.lastResponse = ApiClient.deposit(accountNumber, amount, "ATM");
    }

    @When("I transfer {double} from the first account to the second account")
    public void iTransferBetweenCreatedAccounts(double amount) {
        ctx.lastResponse = ApiClient.transfer(ctx.currentAccountNumber, ctx.secondAccountNumber, amount);
    }

    @When("I transfer {double} from {string} to {string}")
    public void iTransfer(double amount, String from, String to) {
        ctx.lastResponse = ApiClient.transfer(from, to, amount);
    }

    @When("I freeze the created account")
    public void iFreezeCreatedAccount() {
        ctx.lastResponse = ApiClient.freezeAccount(ctx.currentAccountNumber);
    }

    @When("I freeze account {string}")
    public void iFreezeAccount(String accountNumber) {
        ctx.lastResponse = ApiClient.freezeAccount(accountNumber);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        ctx.lastResponse.then().statusCode(status);
    }

    @Then("the account should be created successfully")
    public void theAccountShouldBeCreatedSuccessfully() {
        ctx.lastResponse.then()
                .statusCode(201)
                .body("accountNumber", notNullValue())
                .body("status", equalTo("ACTIVE"))
                .body("balance", notNullValue());
    }

    @Then("the balance after should be {double}")
    public void theBalanceAfterShouldBe(double expected) {
        double actual = ctx.lastResponse.jsonPath().getDouble("balanceAfter");
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Then("the transaction type should be {string}")
    public void theTransactionTypeShouldBe(String type) {
        ctx.lastResponse.then().body("type", equalTo(type));
    }

    @Then("the error message should contain {string}")
    public void theErrorMessageShouldContain(String text) {
        String error = ctx.lastResponse.jsonPath().getString("error");
        Assertions.assertThat(error).containsIgnoringCase(text);
    }

    @Then("the account status should be {string}")
    public void theAccountStatusShouldBe(String status) {
        ctx.lastResponse.then().body("status", equalTo(status));
    }
}

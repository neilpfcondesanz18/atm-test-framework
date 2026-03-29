package com.atm.utils;

import com.atm.config.FrameworkConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ApiClient {

    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);
    private static final FrameworkConfig config = FrameworkConfig.getInstance();
    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;

    static {
        RestAssured.baseURI = config.getBaseUrl();
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification given() {
        return RestAssured.given().spec(requestSpec);
    }

    // ── Accounts ────────────────────────────────────────────────

    public static Response createAccount(Map<String, Object> body) {
        return given().body(body).post("/api/v1/accounts");
    }

    public static Response getAccount(String accountNumber) {
        return given().get("/api/v1/accounts/{number}", accountNumber);
    }

    public static Response getAllAccounts() {
        return given().get("/api/v1/accounts");
    }

    public static Response withdraw(String accountNumber, double amount, String atmId) {
        return given()
                .body(Map.of("amount", amount, "atmId", atmId))
                .post("/api/v1/accounts/{number}/withdraw", accountNumber);
    }

    public static Response deposit(String accountNumber, double amount, String channel) {
        return given()
                .body(Map.of("amount", amount, "channel", channel))
                .post("/api/v1/accounts/{number}/deposit", accountNumber);
    }

    public static Response transfer(String from, String to, double amount) {
        return given()
                .body(Map.of("fromAccountNumber", from, "toAccountNumber", to, "amount", amount))
                .post("/api/v1/accounts/transfer");
    }

    public static Response freezeAccount(String accountNumber) {
        return given().post("/api/v1/accounts/{number}/freeze", accountNumber);
    }

    public static Response getTransactions(String accountNumber) {
        return given().get("/api/v1/accounts/{number}/transactions", accountNumber);
    }

    // ── Cards ────────────────────────────────────────────────────

    public static Response issueCard(String accountNumber, String cardType) {
        return given()
                .body(Map.of("accountNumber", accountNumber, "cardType", cardType))
                .post("/api/v1/cards/issue");
    }

    public static Response getCard(String cardNumber) {
        return given().get("/api/v1/cards/{number}", cardNumber);
    }

    public static Response blockCard(String cardNumber) {
        return given().post("/api/v1/cards/{number}/block", cardNumber);
    }

    public static Response unblockCard(String cardNumber) {
        return given().post("/api/v1/cards/{number}/unblock", cardNumber);
    }

    public static Response validatePin(String cardNumber, String pin) {
        return given()
                .body(Map.of("pin", pin))
                .post("/api/v1/cards/{number}/validate-pin", cardNumber);
    }

    // ── Health ───────────────────────────────────────────────────

    public static Response healthCheck() {
        return given().get("/api/v1/accounts/health");
    }
}
